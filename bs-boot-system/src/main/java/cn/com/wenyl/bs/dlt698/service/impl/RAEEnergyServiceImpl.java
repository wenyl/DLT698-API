package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.service.CarbonDeviceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateContext;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateLog;
import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.service.RAEEnergyService;
import cn.com.wenyl.bs.dlt698.service.RS485Service;
import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class RAEEnergyServiceImpl implements RAEEnergyService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;
    @Override
    @DeviceOperateLog(jobName = "反向有功电能量-获取反向有功电能量",valueSign = "raee",valueLabel = "反向有功电能量",hasValue = true)
    public Object getRAEEnergy(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException {
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = (GetRequestNormalFrame)builder.getFrame(FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, BCDUtils.encodeBCD(carbonDeviceAddress),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO,OI.RAEE, AttrNum.ATTR_02,AttributeIndex.ZERO,TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class);
            GetResponseNormalFrame frame = parser.parseFrame(returnFrame);
            DeviceOperateContext.get().setSentFrame(HexUtils.bytesToHex(bytes));
            DeviceOperateContext.get().setReceivedFrame(HexUtils.bytesToHex(returnFrame));
            if(frame.getNormalData().getDataType() != null){
                if(frame.getNormalData().getDataType().equals(DataType.DOUBLE_LONG)){
                    log.warn("正向有功电能查询返回数据异常!");
                }
                if(frame.getNormalData().getDataType().equals(DataType.ARRAY)){
                    List<Object> ret = new ArrayList<>();
                    JSONArray array = (JSONArray) parser.getData(frame);
                    for (Object o : array) {
                        // 2位小数，但是接口返回是整数，需要自己除100得到2位小数
                        ret.add(((Long) o) / (100.0));
                    }
                    DeviceOperateContext.get().setValueJson(JSON.toJSONString(ret));
                    return ret;
                }
            }
            Object obj = parser.getData(frame);
            DeviceOperateContext.get().setValueJson(JSON.toJSONString(obj));
            return obj;
        } finally{
            SerialCommUtils.getInstance().closePort();
        }
    }
}

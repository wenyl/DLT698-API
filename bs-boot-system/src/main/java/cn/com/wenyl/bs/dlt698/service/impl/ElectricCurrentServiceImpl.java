package cn.com.wenyl.bs.dlt698.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import cn.com.wenyl.bs.dlt698.annotation.CarbonDeviceAddress;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateContext;
import cn.com.wenyl.bs.dlt698.annotation.DeviceOperateLog;
import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.service.ElectricCurrentService;
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
public class ElectricCurrentServiceImpl implements ElectricCurrentService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;
    @Override
    @DeviceOperateLog(jobName = "电流-读取电流",valueSign = "electricCurrent",valueLabel = "电流",hasValue = true)
    public Object getElectricCurrent(@CarbonDeviceAddress String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException {
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = (GetRequestNormalFrame)builder.getFrame(FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, BCDUtils.encodeBCD(carbonDeviceAddress),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO,OI.ELECTRIC_CURRENT, AttrNum.ATTR_02,AttributeIndex.ZERO_ONE,TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            log.info("收到数据帧{}", HexUtils.bytesToHex(returnFrame));
            GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class);
            GetResponseNormalFrame frame = parser.parseFrame(returnFrame);

            DeviceOperateContext.get().setSentFrame(HexUtils.bytesToHex(bytes));
            DeviceOperateContext.get().setReceivedFrame(HexUtils.bytesToHex(returnFrame));

            if(frame.getNormalData().getDataType() != null){
                if(frame.getNormalData().getDataType().equals(DataType.DOUBLE_LONG)){
                    return (Integer)parser.getData(frame)/1000.0;
                }
                if(frame.getNormalData().getDataType().equals(DataType.ARRAY)){
                    List<Object> ret = new ArrayList<>();
                    JSONArray array = (JSONArray) parser.getData(frame);
                    for (Object o : array) {
                        // 这个电表分项电流是三位小数，但是接口返回是整数，需要自己除1000得到三位小数
                        ret.add(((Integer) o) / (1000.0));
                    }

                    DeviceOperateContext.get().setValueJson(JSON.toJSONString(ret));
                    return ret;
                }
            }
            Object obj = parser.getData(parser.parseFrame(returnFrame));
            DeviceOperateContext.get().setValueJson(JSON.toJSONString(obj));
            return obj;
        } finally{
            SerialCommUtils.getInstance().closePort();
        }
    }
}

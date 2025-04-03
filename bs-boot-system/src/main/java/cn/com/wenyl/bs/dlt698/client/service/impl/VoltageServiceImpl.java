package cn.com.wenyl.bs.dlt698.client.service.impl;

import cn.com.wenyl.bs.dlt698.client.constants.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import cn.com.wenyl.bs.dlt698.client.annotation.CarbonDeviceAddress;
import cn.com.wenyl.bs.dlt698.client.annotation.DeviceOperateContext;
import cn.com.wenyl.bs.dlt698.client.annotation.DeviceOperateLog;

import cn.com.wenyl.bs.dlt698.client.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.client.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.client.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.client.service.RS485Service;
import cn.com.wenyl.bs.dlt698.client.service.VoltageService;
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
public class VoltageServiceImpl implements VoltageService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;
    @Override
    @DeviceOperateLog(jobName = "电压-读取电压",valueSign = "voltage",valueLabel = "电压",hasValue = true)
    public Object getVoltage(@CarbonDeviceAddress String carbonDeviceAddress) throws JSONException,ExecutionException, InterruptedException, TimeoutException {
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = (GetRequestNormalFrame)builder.getFrame(FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, BCDUtils.encodeBCD(carbonDeviceAddress),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO,OI.VOLTAGE, AttrNum.ATTR_02,AttributeIndex.ZERO,TimeTag.NO_TIME_TAG);
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
                    log.warn("分项电压查询返回数据异常!");
                }
                if(frame.getNormalData().getDataType().equals(DataType.ARRAY)){
                    List<Object> ret = new ArrayList<>();
                    JSONArray array = (JSONArray) parser.getData(frame);
                    for (Object o : array) {
                        ret.add(((Integer) o) / (10.0));
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

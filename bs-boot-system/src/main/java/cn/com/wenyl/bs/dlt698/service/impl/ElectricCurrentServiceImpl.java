package cn.com.wenyl.bs.dlt698.service.impl;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public Object getElectricCurrent(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException {
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
            if(frame.getNormalData().getDataType() != null){
                if(frame.getNormalData().getDataType().equals(DataType.DOUBLE_LONG)){
                    log.warn("分项电流查询返回数据异常!");
                }
                if(frame.getNormalData().getDataType().equals(DataType.ARRAY)){
                    JSONArray ret = new JSONArray();
                    JSONArray array = (JSONArray) parser.getData(frame);
                    for (int i = 0; i < array.length(); i++) {
                        // 这个电表分项电流是三位小数，但是接口返回是整数，需要自己除1000得到三位小数
                        ret.put(((Long) array.get(i))/1000);
                    }
                    return ret;
                }
            }
            return parser.getData(frame);
        } finally{
            SerialCommUtils.getInstance().closePort();
        }
    }
}

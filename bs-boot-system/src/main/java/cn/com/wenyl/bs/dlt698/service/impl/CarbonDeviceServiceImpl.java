package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.*;
import cn.com.wenyl.bs.dlt698.service.*;
import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
@Slf4j
@Service("carbonDeviceService")
public class CarbonDeviceServiceImpl implements ICarbonDeviceService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;

    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;


    @Override
    public Object getCarbonDeviceAddress() throws RuntimeException,TimeoutException, ExecutionException, InterruptedException {
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = (GetRequestNormalFrame)builder.getFrame(FunctionCode.THREE,ScramblingCodeFlag.NOT_SCRAMBLING_CODE,FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST,AddressType.DISTRIBUTION_ADDRESS,LogicAddress.ZERO,Address.DISTRIBUTION_ADDRESS,
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO,OI.MAIL_ADDRESS, AttrNum.ATTR_4001_02,AttributeIndex.ZERO,TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            log.info("收到数据帧{}", HexUtils.bytesToHex(returnFrame));
            GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class);
            return parser.getData(parser.parseFrame(returnFrame));
        } finally{
            SerialCommUtils.getInstance().closePort();
        }
    }

    @Override
    public Object connectCarbonDevice(String carbonDeviceAddress) throws RuntimeException,TimeoutException, ExecutionException, InterruptedException  {
        ConnectRequestFrameBuilder builder = (ConnectRequestFrameBuilder)frameBuildProcessor.getFrameBuilder(ConnectRequestFrame.class);
        ConnectRequestFrame connectRequestFrame = (ConnectRequestFrame)builder.getFrame(FunctionCode.THREE,ScramblingCodeFlag.NOT_SCRAMBLING_CODE,FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST,AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, BCDUtils.encodeBCD(carbonDeviceAddress),Address.CLIENT_ADDRESS);
        // 这里使用了默认的参数，有其他需要自己设置对应值即可
        ConnectRequestData requestData = new ConnectRequestData(PIID.ZERO_ZERO,TimeTag.NO_TIME_TAG);
        connectRequestFrame.setConnectRequestData(requestData);
        byte[] bytes = builder.buildFrame(connectRequestFrame);
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            log.info("收到数据帧{}", HexUtils.bytesToHex(returnFrame));
            ConnectResponseFrameParser parser = (ConnectResponseFrameParser)frameParseProcessor.getFrameParser(ConnectResponseFrame.class);
            return parser.getData(parser.parseFrame(returnFrame));
        } finally{
            SerialCommUtils.getInstance().closePort();
        }
    }
}

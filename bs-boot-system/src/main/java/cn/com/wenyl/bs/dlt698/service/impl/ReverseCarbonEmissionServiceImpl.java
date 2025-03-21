package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.service.ReverseCarbonEmissionService;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.service.RS485Service;
import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ReverseCarbonEmissionServiceImpl implements ReverseCarbonEmissionService {
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;
    @Override
    public Object yesterdayCarbonAccumulate(String carbonDeviceAddress)  throws ExecutionException, InterruptedException, TimeoutException {
        // 先链接电表
        carbonDeviceService.connectCarbonDevice(carbonDeviceAddress);
        // 查询昨日碳排放累计量
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = (GetRequestNormalFrame)builder.getFrame(FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, BCDUtils.encodeBCD(carbonDeviceAddress),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO,OI.REVERSE_CARBON_EMISSION, AttrNum.ATTR_02,AttributeIndex.ZERO,TimeTag.NO_TIME_TAG);
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
}

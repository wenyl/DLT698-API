package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.*;
import cn.com.wenyl.bs.dlt698.service.*;
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
    private FrameParseProcessor frameParse;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;


    @Override
    public Object getCarbonDeviceAddress() throws RuntimeException,TimeoutException, ExecutionException, InterruptedException {
        GetRequestNormalFrame getRequestNormalFrame = new GetRequestNormalFrame();
        ControlDomain controlDomain = new ControlDomain();
        controlDomain.setFunCode(FunctionCode.THREE);
        controlDomain.setSc(ScramblingCodeFlag.NOT_SCRAMBLING_CODE);
        controlDomain.setFrameFlg(FrameFlag.NOT_SUB_FRAME);
        RequestType requestType = RequestType.CLIENT_REQUEST;
        controlDomain.setPrm(requestType.getPrm());
        controlDomain.setDir(requestType.getDir());
        getRequestNormalFrame.setControlDomain(controlDomain);

        AddressDomain addressDomain = new AddressDomain();
        addressDomain.setAddressType(AddressType.DISTRIBUTION_ADDRESS);
        addressDomain.setLogicAddress(LogicAddress.ZERO);
        addressDomain.setServerAddress(Address.DISTRIBUTION_ADDRESS);
        addressDomain.setClientAddress(Address.CLIENT_ADDRESS);
        getRequestNormalFrame.setAddressDomain(addressDomain);

        GetRequestNormalData userData = new GetRequestNormalData();
        userData.setApdu(ClientAPDU.GET_REQUEST.getSign());
        userData.plusLength();
        userData.setOpera(GetRequest.GET_REQUEST_NORMAL.getSign());
        userData.plusLength();
        userData.setPIID(PIID.ZERO_ZERO);
        userData.plusLength();
        byte[] oi = OI.MAIL_ADDRESS.getSign();
        byte[] oad = new byte[4];
        oad[0] = oi[0];
        oad[1] = oi[1];
        oad[2] = AttributeNumberFeatures.TWO_ZERO;
        oad[3] = AttributeIndex.ZERO;
        userData.setOad(oad);
        userData.plusLength(oad.length);
        userData.setTimeTag(TimeTag.NO_TIME_TAG);
        userData.plusLength();
        getRequestNormalFrame.setData(userData);

        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);
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
    public Object connectCarbonDevice(String carbonDeviceAddress) {

        return null;
    }
}

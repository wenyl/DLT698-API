package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.APDU;
import cn.com.wenyl.bs.dlt698.entity.CSInfo;
import cn.com.wenyl.bs.dlt698.entity.FrameData;
import cn.com.wenyl.bs.dlt698.service.FrameBuildService;
import cn.com.wenyl.bs.dlt698.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.service.ICarbonDeviceService;
import cn.com.wenyl.bs.dlt698.service.RS485Service;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import cn.com.wenyl.bs.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
@Slf4j
@Service("carbonDeviceService")
public class CarbonDeviceServiceImpl implements ICarbonDeviceService {

    @Resource
    private FrameBuildService frameBuildService;
    @Resource
    private FrameParseService frameParseService;
    @Resource
    private RS485Service rs485Service;
    @Resource
    private UserDataParseServiceImpl userDataParseService;

    @Override
    public JSONObject getCarbonDeviceAddress() throws RuntimeException,TimeoutException, ExecutionException, InterruptedException {
        CSInfo csInfo = new CSInfo();
        csInfo.setFunCode(FunctionCode.THREE);
        csInfo.setSc(ScramblingCodeFlag.NOT_SCRAMBLING_CODE);
        csInfo.setFrameFlg(FrameFlag.NOT_SUB_FRAME);
        RequestType requestType = RequestType.CLIENT_REQUEST;
        csInfo.setPrm(requestType.getPrm());
        csInfo.setDir(requestType.getDir());
        csInfo.setAType(AddressType.DISTRIBUTION_ADDRESS);
        csInfo.setLa(LogicAddress.ZERO);
        csInfo.setSa(Address.DISTRIBUTION_ADDRESS);
        csInfo.setCa(Address.CLIENT_ADDRESS);

        APDU apdu = new APDU();
        apdu.setApdu(ClientAPDU.GET_REQUEST.getSign());
        apdu.plusByteLength();
        apdu.setRequest(GetRequest.GET_REQUEST_NORMAL.getSign());
        apdu.plusByteLength();
        apdu.setPiid(PIID.ZERO_ZERO);
        apdu.plusByteLength();
        byte[] oi = OI.MAIL_ADDRESS.getSign();
        byte[] oad = new byte[4];
        oad[0] = oi[0];
        oad[1] = oi[1];
        oad[2] = AttributeNumberFeatures.TWO_ZERO;
        oad[3] = AttributeIndex.ZERO;
        apdu.setOad(oad);
        apdu.plusByteLength(oad.length);
        apdu.setTimeTag(TimeTag.NO_TIME_TAG);
        apdu.plusByteLength();
        byte[] bytes = frameBuildService.buildFrame(csInfo,apdu);
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            log.info("收到数据帧{}", HexUtils.bytesToHex(returnFrame));
            FrameData frameData = frameParseService.parseFrame(returnFrame);
            return userDataParseService.parseUserData(frameData);
        } finally{
            SerialCommUtils.getInstance().closePort();
        }
    }
}

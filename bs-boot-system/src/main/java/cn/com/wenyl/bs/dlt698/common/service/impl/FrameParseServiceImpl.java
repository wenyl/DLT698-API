package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseData;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseFrame;
import cn.com.wenyl.bs.dlt698.common.entity.OAD;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceErrorMsgHis;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.net4g.service.*;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.ASN1EncoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 解析服务
 */
@Slf4j
@Service("frameParseServiceImpl")
public class FrameParseServiceImpl implements FrameParseService {


    @Resource
    private CarbonDeviceService carbonDeviceService;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Resource
    private LinkService linkService;
    @Resource
    private VoltageService voltageService;
    @Resource
    private ElectricService electricService;
    @Resource
    private ForwardActivePowerService forwardActivePowerService;
    @Resource
    private ReverseActivePowerService reverseActivePowerService;
    @Resource
    private DeviceErrorMsgHisService deviceErrorMsgHisService;
    @Resource
    private ForwardCarbonEmissionService forwardCarbonEmissionService;
    @Resource
    private ReverseCarbonEmissionService reverseCarbonEmissionService;
    @Resource
    private CarbonFactorService carbonFactorService;
    @Resource
    private DeviceMsgHisService deviceMsgHisService;

    @Override
    public void frameParse(Integer msgId,FrameDto frameDto,String deviceIp,byte[] bytes) throws Exception {
        byte[] userData = frameDto.getUserData();
        // 如果是碳表发起的请求，则要封装对应数据回复给碳表
        ClientAPDU clientAPDU = frameDto.getClientAPDU();
        ServerAPDU serverAPDU = frameDto.getServerAPDU();
        // 这里处理碳表发来的请求
        if(clientAPDU != ClientAPDU.UNKNOWN){
            switch (clientAPDU){
                case GET_REQUEST:
                case SET_REQUEST:
                case CONNECT_REQUEST:
                    break;
                case LINK_REQUEST:
                    LinkRequestType linkRequestType = LinkRequestType.getLinkRequestTypeBySign(userData[2]);
                    switch (linkRequestType){
                        case LOGIN:
                            linkService.login(deviceIp,frameDto);
                            return;
                        case HEARTBEAT:
                            linkService.heartbeat(deviceIp,frameDto);
                            return;
                        case LOGOUT:
                            linkService.logout(deviceIp,frameDto);
                            deviceChannelManager.removeDevice(deviceIp);
                            return;
                        default:
                    }
                    break;
            }
        }

        // 碳表响应的数据
        if(serverAPDU != ServerAPDU.UNKNOWN){
            switch (serverAPDU){
                case LINK_RESPONSE:
                case CONNECT_RESPONSE:
                    break;
                case GET_RESPONSE:
                    GetResponse getResponse = GetResponse.getResponseBySign(userData[1]);
                    switch (getResponse){
                        case UNKNOWN:
                            throw new RuntimeException("未知GetResponse类型"+HexUtils.byteToHex(userData[1]));
                        case GET_RESPONSE_NORMAL:
                            byte[] oadBytes = new byte[4];
                            System.arraycopy(userData,3,oadBytes,0,4);
                            OAD oad = FrameParseUtils.parseOAD(oadBytes);
                            OI oi = oad.getOi();
                            switch (oi){
                                case UNKNOWN:
                                    break;
                                case MAIL_ADDRESS:
                                    carbonDeviceService.getAddress(deviceIp,frameDto);
                                    break;
                                case ELECTRIC_CURRENT:
                                    electricService.getElectric(deviceChannelManager.getDeviceId(deviceIp),msgId,frameDto);
                                    break;
                                case VOLTAGE:
                                    voltageService.getVoltage(deviceChannelManager.getDeviceId(deviceIp),msgId,frameDto);
                                    break;
                                case PAEE:
                                    forwardActivePowerService.getForwardActivePower(deviceChannelManager.getDeviceId(deviceIp),msgId,frameDto);
                                    break;
                                case RAEE:
                                    reverseActivePowerService.getReverseActivePower(deviceChannelManager.getDeviceId(deviceIp),msgId,frameDto);
                                    break;
                                case FORWARD_CARBON_EMISSION:
                                    forwardCarbonEmissionService.  getForwardCarbonEmission(deviceChannelManager.getDeviceId(deviceIp),msgId,frameDto);
                                    break;
                                case REVERSE_CARBON_EMISSION:
                                    reverseCarbonEmissionService.getReverseCarbonEmission(deviceChannelManager.getDeviceId(deviceIp),msgId,frameDto);
                                    break;
                            }
                            break;
                    }
                    break;
                case SET_RESPONSE:
                    SetResponse setResponse = SetResponse.getSetResponseBySign(userData[1]);
                    switch (setResponse){
                        case UNKNOWN:
                            throw new RuntimeException("未知SetResponse类型"+HexUtils.byteToHex(userData[1]));
                        case SET_RESPONSE_NORMAL:
                            byte[] oadBytes = new byte[4];
                            System.arraycopy(userData,3,oadBytes,0,4);
                            OAD oad = FrameParseUtils.parseOAD(oadBytes);
                            OI oi = oad.getOi();
                            switch(oi){
                                case UNKNOWN:
                                    break;
                                case SET_CARBON_FACTOR:
                                    carbonFactorService.getSetResult(deviceChannelManager.getDeviceId(deviceIp),msgId,frameDto);
                            }
                    }
                    break;
                case PROXY_RESPONSE:
                    ProxyResponse proxyResponse = ProxyResponse.getResponseBySign(userData[1]);
                    switch (proxyResponse){
                        case UNKNOWN:
                            throw new RuntimeException("proxyResponse"+HexUtils.byteToHex(userData[1]));

                        case PROXY_TRANS_COMMAND_RESPONSE:
                            if(userData[7] == GetResultType.DATA.getSign()){
                                int dataLength = userData[8];
                                // 去掉四个FE
                                byte[] originResponse = new byte[dataLength-4];
                                System.arraycopy(userData,13,originResponse,0,dataLength-4);
                                this.parseBytes(deviceIp,originResponse);
                            }
                    }
                    break;

            }
        }
    }

    @Override
    public void parseBytes(String deviceIp, byte[] bytes){
        Integer deviceId = deviceChannelManager.getDeviceId(deviceIp);
        FrameDto frameDto;
        try{
            if(!FrameParseUtils.checkFrame(bytes)){
                String errorInfo = "无效帧：起始符或结束符错误,当前帧起始符--"+HexUtils.byteToHex(bytes[0])+",结束符--"+HexUtils.byteToHex(bytes[bytes.length-1]);
                log.error(errorInfo);
                return;
            }

            if(deviceId == null){
                log.error("未知设备ip={}",deviceIp);
                return;
            }
            frameDto = FrameParseUtils.getFrameDto(bytes);
        }catch (Exception e){
            DeviceErrorMsgHis errorMsgHis = new DeviceErrorMsgHis();
            errorMsgHis.setDeviceId(deviceId);
            errorMsgHis.setByteData(HexUtils.bytesToHex(bytes));
            errorMsgHis.setDataLength(bytes.length);
            errorMsgHis.setCreateTime(LocalDateTime.now());
            errorMsgHis.setErrorMsg(e.getMessage());
            deviceErrorMsgHisService.save(errorMsgHis);
            log.error("帧数据解析报错--{}",e.getMessage());
            return;
        }
        try {
            // 根据IP查询数据
            Integer msgId = deviceMsgHisService.save(frameDto,deviceId,bytes);
            this.frameParse(msgId,frameDto,deviceIp,bytes);
        } catch (Exception e) {
            DeviceErrorMsgHis errorMsgHis = new DeviceErrorMsgHis();
            errorMsgHis.setDeviceId(deviceId);
            errorMsgHis.setByteData(HexUtils.bytesToHex(bytes));
            errorMsgHis.setDataLength(bytes.length);
            errorMsgHis.setCreateTime(LocalDateTime.now());
            errorMsgHis.setErrorMsg(e.getMessage());
            deviceErrorMsgHisService.save(errorMsgHis);
            log.error("帧数据保存解析解析报错--{}",e.getMessage());
            log.error("解析收到的帧数据异常，设备ID: {}, 字节内容: {}", deviceIp, HexUtils.bytesToHex(bytes), e);
        }
    }
}

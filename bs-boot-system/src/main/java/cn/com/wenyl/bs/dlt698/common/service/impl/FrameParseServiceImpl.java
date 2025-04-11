package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseData;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseFrame;
import cn.com.wenyl.bs.dlt698.common.entity.OAD;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.ASN1EncoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 解析服务
 */
@Slf4j
@Service("frameParseServiceImpl")
public class FrameParseServiceImpl implements FrameParseService {
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    /**
     * 解析LinkRequest 的login类型
     */
    @Override
    public void login(String deviceIp,FrameDto frameDto) throws Exception {
        byte[] receiveTime = ASN1EncoderUtils.encodeDateTime();
        LinkRequestFrameParser frameParser = (LinkRequestFrameParser)frameParseProcessor.getFrameParser(LinkRequestFrame.class, LinkRequestData.class);
        LinkRequestFrame linkRequestFrame = frameParser.parseFrame(frameDto);
        byte[] requestTime = linkRequestFrame.getLinkRequestData().getRequestTimeBytes();
        LinkResponseFrameBuilder frameBuilder = (LinkResponseFrameBuilder) frameBuildProcessor.getFrameBuilder(LinkResponseFrame.class);

        LinkResponseFrame linkResponseFrame = FrameBuildUtils.getCommonFrame(LinkResponseFrame.class,FunctionCode.ONE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME
                ,RequestType.CLIENT_RESPONSE_SERVER, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO,linkRequestFrame.getAddressDomain().getServerAddress(),Address.CLIENT_ADDRESS);
        LinkResponseData linkResponseData = new LinkResponseData();
        linkResponseData.setApdu(ServerAPDU.LINK_RESPONSE.getSign());
        linkResponseData.setPIID(PIID.ZERO_ZERO);
        linkResponseData.setRequestTimeBytes(requestTime);
        linkResponseData.setReceiveTimeBytes(receiveTime);
        linkResponseFrame.setLinkUserData(linkResponseData);
        byte[] frameBytes = frameBuilder.buildFrame(linkResponseFrame);
        deviceChannelManager.sendDataToDevice(deviceIp,frameBytes);
    }
    /**
     * 解析LinkRequest类型
     */
    @Override
    public void heartbeat(String deviceIp,FrameDto frameDto) throws Exception {
        byte[] receiveTime = ASN1EncoderUtils.encodeDateTime();
        LinkRequestFrameParser frameParser = (LinkRequestFrameParser)frameParseProcessor.getFrameParser(LinkRequestFrame.class, LinkRequestData.class);
        LinkRequestFrame linkRequestFrame = frameParser.parseFrame(frameDto);
        byte[] requestTime = linkRequestFrame.getLinkRequestData().getRequestTimeBytes();
        LinkResponseFrameBuilder frameBuilder = (LinkResponseFrameBuilder) frameBuildProcessor.getFrameBuilder(LinkResponseFrame.class);

        LinkResponseFrame linkResponseFrame = FrameBuildUtils.getCommonFrame(LinkResponseFrame.class,FunctionCode.ONE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME
                ,RequestType.CLIENT_RESPONSE_SERVER, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO,linkRequestFrame.getAddressDomain().getServerAddress(),Address.CLIENT_ADDRESS);
        LinkResponseData linkResponseData = new LinkResponseData();
        linkResponseData.setApdu(ServerAPDU.LINK_RESPONSE.getSign());
        linkResponseData.setPIID(PIID.ZERO_ZERO);
        linkResponseData.setRequestTimeBytes(requestTime);
        linkResponseData.setReceiveTimeBytes(receiveTime);
        linkResponseFrame.setLinkUserData(linkResponseData);
        byte[] frameBytes = frameBuilder.buildFrame(linkResponseFrame);
        deviceChannelManager.sendDataToDevice(deviceIp,frameBytes);
    }

    @Override
    public void logout(String deviceIp,FrameDto frameDto) throws Exception {
        byte[] receiveTime = ASN1EncoderUtils.encodeDateTime();
        LinkRequestFrameParser frameParser = (LinkRequestFrameParser)frameParseProcessor.getFrameParser(LinkRequestFrame.class, LinkRequestData.class);
        LinkRequestFrame linkRequestFrame = frameParser.parseFrame(frameDto);
        byte[] requestTime = linkRequestFrame.getLinkRequestData().getRequestTimeBytes();
        LinkResponseFrameBuilder frameBuilder = (LinkResponseFrameBuilder) frameBuildProcessor.getFrameBuilder(LinkResponseFrame.class);

        LinkResponseFrame linkResponseFrame = FrameBuildUtils.getCommonFrame(LinkResponseFrame.class,FunctionCode.ONE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME
                ,RequestType.CLIENT_RESPONSE_SERVER, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO,linkRequestFrame.getAddressDomain().getServerAddress(),Address.CLIENT_ADDRESS);
        LinkResponseData linkResponseData = new LinkResponseData();
        linkResponseData.setApdu(ServerAPDU.LINK_RESPONSE.getSign());
        linkResponseData.setPIID(PIID.ZERO_ZERO);
        linkResponseData.setRequestTimeBytes(requestTime);
        linkResponseData.setReceiveTimeBytes(receiveTime);
        linkResponseFrame.setLinkUserData(linkResponseData);
        byte[] frameBytes = frameBuilder.buildFrame(linkResponseFrame);
        deviceChannelManager.sendDataToDevice(deviceIp,frameBytes);
    }

    @Override
    public void frameParse(FrameDto frameDto,String deviceIp,byte[] bytes) throws Exception {
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
                            login(deviceIp,frameDto);
                            return;
                        case HEARTBEAT:
                            heartbeat(deviceIp,frameDto);
                            return;
                        case LOGOUT:
                            logout(deviceIp,frameDto);
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
                                case ELECTRIC_CURRENT:
                                    // todo 电流
                                case VOLTAGE:
                                    // todo 电压
                                case PAEE:
                                    // todo 正向有功电能
                                case RAEE:
                                    // todo 反向有功电能
                                case FORWARD_CARBON_EMISSION:
                                    // todo 正向碳排放量
                                case REVERSE_CARBON_EMISSION:
                                    // todo 反向碳排放量
                            }
                            break;
                    }
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
                                    // todo设置电碳因子
                            }
                    }

            }
        }

    }
}

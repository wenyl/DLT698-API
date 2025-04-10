package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseData;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.service.LinkRequestFrameParser;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.ASN1EncoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

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
    public void login(String deviceIp,FrameDto frameDto) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
    public void heartbeat(String deviceIp,FrameDto frameDto) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
    public void logout(String deviceIp,FrameDto frameDto) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
    public void frameParse(FrameDto frameDto,String deviceIp,byte[] bytes) throws RuntimeException,InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        byte[] userData = frameDto.getUserData();
        // 如果是碳表发起的请求，则要封装对应数据回复给碳表
        ClientAPDU clientAPDU = frameDto.getClientAPDU();
        ServerAPDU serverAPDU = frameDto.getServerAPDU();
        // 这里处理碳表发来的请求
        if(clientAPDU != ClientAPDU.UNKNOWN){
            switch (clientAPDU){
                case GET_REQUEST:
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
                            String msg = "未定义的LinkRequestType类型"+ HexUtils.byteToHex(userData[2]);
                            log.error(msg);
                            throw new RuntimeException(msg);
                    }
                case CONNECT_REQUEST:
                    break;
                case SET_REQUEST:
                    break;
            }
        }

        // todo 处理碳表对服务的响应
    }
}

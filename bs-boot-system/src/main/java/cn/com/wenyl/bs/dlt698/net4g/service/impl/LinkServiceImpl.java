package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseData;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.impl.FrameBuildProcessor;
import cn.com.wenyl.bs.dlt698.common.service.impl.LinkRequestFrameParser;
import cn.com.wenyl.bs.dlt698.common.service.impl.LinkResponseFrameBuilder;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.net4g.service.LinkService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.ASN1EncoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LinkServiceImpl implements LinkService {
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
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

        LinkResponseFrame linkResponseFrame = FrameBuildUtils.getCommonFrame(LinkResponseFrame.class, FunctionCode.ONE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME
                , RequestType.CLIENT_RESPONSE_SERVER, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO,linkRequestFrame.getAddressDomain().getServerAddress(),Address.CLIENT_ADDRESS);
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
}

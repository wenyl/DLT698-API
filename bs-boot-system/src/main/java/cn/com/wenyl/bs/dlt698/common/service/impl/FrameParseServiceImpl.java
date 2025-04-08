package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.client.service.impl.FrameBuildProcessor;
import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseData;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.server.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.server.service.LinkRequestFrameParser;
import cn.com.wenyl.bs.dlt698.server.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.server.tcp.NettyTcpServer;
import cn.com.wenyl.bs.dlt698.server.tcp.TcpServerHandler;
import cn.com.wenyl.bs.dlt698.utils.ASN1EncoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
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
    private TcpServerHandler tcpServerHandler;
    /**
     * 解析LinkRequest类型
     */
    @Override
    public void linkRequest(String deviceId,FrameDto frameDto,byte[] bytes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        byte[] receiveTime = ASN1EncoderUtils.encodeDateTime();
        LinkRequestFrameParser frameParser = (LinkRequestFrameParser)frameParseProcessor.getFrameParser(LinkRequestFrame.class, LinkRequestData.class);
        LinkRequestFrame linkRequestFrame = frameParser.parseFrame(bytes);
        byte[] requestTime = linkRequestFrame.getLinkRequestData().getRequestTimeBytes();
        LinkResponseFrameBuilder frameBuilder = (LinkResponseFrameBuilder) frameBuildProcessor.getFrameBuilder(LinkResponseFrame.class);

        LinkResponseFrame linkResponseFrame = (LinkResponseFrame) FrameBuildUtils.getCommonFrame(LinkResponseFrame.class,FunctionCode.ONE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME
                ,RequestType.CLIENT_RESPONSE_SERVER, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO,linkRequestFrame.getAddressDomain().getServerAddress(),Address.CLIENT_ADDRESS);
        LinkResponseData linkResponseData = new LinkResponseData();
        linkResponseData.setApdu(ServerAPDU.LINK_RESPONSE.getSign());
        linkResponseData.setPIID(PIID.ZERO_ZERO);
        linkResponseData.setRequestTimeBytes(requestTime);
        linkResponseData.setReceiveTimeBytes(receiveTime);
        linkResponseFrame.setLinkUserData(linkResponseData);
        byte[] frameBytes = frameBuilder.buildFrame(linkResponseFrame);
        tcpServerHandler.sendDataToDevice(deviceId,frameBytes);
    }

    /**
     * 解析接收到的数据，调用对应的服务
     * @param bytes 接收的数据
     */
    @Override
    public void frameParse(String deviceId,byte[] bytes) throws RuntimeException,InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        FrameDto frameDto = FrameParseUtils.getFrameDto(bytes);
        byte[] userData = frameDto.getUserData();
        // 如果是碳表发起的请求，则要封装对应数据回复给碳表
        ClientAPDU clientAPDU = ClientAPDU.getClientAPDUBySign(userData[0]);
        if(clientAPDU == null){
            String msg = "无法解析的请求类型"+ HexUtils.byteToHex(userData[0]);
            log.error(msg);
            throw new RuntimeException(msg);
        }
        switch (clientAPDU){
            case GET_REQUEST:
                break;
            case LINK_REQUEST:
                linkRequest(deviceId,frameDto,bytes);
            case CONNECT_REQUEST:
                break;
            case SET_REQUEST:
                break;
        }
    }
}

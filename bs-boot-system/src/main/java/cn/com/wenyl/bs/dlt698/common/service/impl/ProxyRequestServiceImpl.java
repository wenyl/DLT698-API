package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.ProxyTransCommandRequestData;
import cn.com.wenyl.bs.dlt698.common.entity.ProxyTransCommandRequestFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.ProxyRequestService;
import cn.com.wenyl.bs.dlt698.net4g.entity.CarbonFactor;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonFactorService;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class ProxyRequestServiceImpl implements ProxyRequestService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Resource
    private DeviceMsgHisService deviceMsgHisService;
    @Resource
    @Lazy
    private CarbonFactorService carbonFactorService;
    @Override
    public void proxyCmd(String deviceIp,byte[] proxyFrame) throws Exception{

        // 保存代理请求包装的数据
        FrameDto frameDto = FrameParseUtils.getFrameDto(proxyFrame);
        Integer deviceId = deviceChannelManager.getDeviceId(deviceIp);
        deviceMsgHisService.save(frameDto,deviceId,proxyFrame);

        // 构建代理帧
        ProxyTransCommandRequestFrameBuilder builder = (ProxyTransCommandRequestFrameBuilder)frameBuildProcessor.getFrameBuilder(ProxyTransCommandRequestFrame.class);
        ProxyTransCommandRequestFrame proxyTransCommandRequestFrame = FrameBuildUtils.getCommonFrame(ProxyTransCommandRequestFrame.class,FunctionCode.THREE,ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.DISTRIBUTION_ADDRESS, LogicAddress.ZERO, Address.DISTRIBUTION_ADDRESS,
                Address.CLIENT_ADDRESS);
        ProxyTransCommandRequestData data = new ProxyTransCommandRequestData(PIID.ZERO_FIVE, OI.RS485, AttrNum.ATTR_02, AttributeIndex.ZERO_ONE.getSign(),proxyFrame, TimeTag.NO_TIME_TAG);
        proxyTransCommandRequestFrame.setData(data);
        byte[] bytes = builder.buildFrame(proxyTransCommandRequestFrame);
        deviceChannelManager.sendDataToDevice(deviceIp,bytes);
    }

    public void setProxyCmd(String deviceIp,byte[] proxyFrame,String setData) throws Exception{
        // 保存代理请求包装的数据
        FrameDto frameDto = FrameParseUtils.getFrameDto(proxyFrame);
        Integer deviceId = deviceChannelManager.getDeviceId(deviceIp);
        Integer msgId = deviceMsgHisService.save(frameDto, deviceId, proxyFrame);

        ClientAPDU clientAPDU = frameDto.getClientAPDU();
        if(clientAPDU != ClientAPDU.SET_REQUEST){
            throw new RuntimeException("客户端设置报文APDU异常,当前APDU="+HexUtils.byteToHex(clientAPDU.getSign()));
        }
        if(SetRequest.getSetRequestBySign(frameDto.getUserData()[1]) != SetRequest.SET_REQUEST_NORMAL){
            throw new RuntimeException("主站目前仅支持SetRequestNormal类型报文，当前报文类型"+HexUtils.byteToHex(frameDto.getUserData()[1]));
        }

        // 当前仅支持设置碳因子
        CarbonFactor carbonFactor = new CarbonFactor();
        carbonFactor.setCarbonFactor(setData);
        carbonFactor.setDeviceId(deviceId);
        carbonFactor.setMsgId(msgId);
        carbonFactor.setDateTime(LocalDateTime.now());
        carbonFactorService.save(carbonFactor);
        // 构建代理帧
        ProxyTransCommandRequestFrameBuilder builder = (ProxyTransCommandRequestFrameBuilder)frameBuildProcessor.getFrameBuilder(ProxyTransCommandRequestFrame.class);
        ProxyTransCommandRequestFrame proxyTransCommandRequestFrame = FrameBuildUtils.getCommonFrame(ProxyTransCommandRequestFrame.class,FunctionCode.THREE,ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.DISTRIBUTION_ADDRESS, LogicAddress.ZERO, Address.DISTRIBUTION_ADDRESS,
                Address.CLIENT_ADDRESS);
        ProxyTransCommandRequestData data = new ProxyTransCommandRequestData(PIID.ZERO_FIVE, OI.RS485, AttrNum.ATTR_02, AttributeIndex.ZERO_ONE.getSign(),proxyFrame, TimeTag.NO_TIME_TAG);
        proxyTransCommandRequestFrame.setData(data);
        byte[] bytes = builder.buildFrame(proxyTransCommandRequestFrame);
        deviceChannelManager.sendDataToDevice(deviceIp,bytes);
    }
}

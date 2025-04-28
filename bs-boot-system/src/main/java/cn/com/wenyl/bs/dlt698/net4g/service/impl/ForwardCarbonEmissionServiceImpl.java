package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.GetResponseNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.ProxyRequestService;
import cn.com.wenyl.bs.dlt698.common.service.impl.FrameBuildProcessor;
import cn.com.wenyl.bs.dlt698.common.service.impl.GetRequestNormalFrameBuilder;
import cn.com.wenyl.bs.dlt698.common.service.impl.GetResponseNormalFrameParser;
import cn.com.wenyl.bs.dlt698.net4g.entity.ForwardCarbonEmission;
import cn.com.wenyl.bs.dlt698.net4g.mapper.ForwardCarbonEmissionMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.ForwardCarbonEmissionService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 正向碳排放 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class ForwardCarbonEmissionServiceImpl extends ServiceImpl<ForwardCarbonEmissionMapper, ForwardCarbonEmission> implements ForwardCarbonEmissionService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @Resource
    private ProxyRequestService proxyRequestService;
    @Override
    public void getForwardCarbonEmission(String deviceIp) throws Exception {
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = FrameBuildUtils.getCommonFrame(GetRequestNormalFrame.class, FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS, LogicAddress.ZERO, carbonDeviceService.getDeviceAddress(deviceIp),
                Address.CLIENT_ADDRESS);
        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO, OI.FORWARD_CARBON_EMISSION, AttrNum.ATTR_02,AttributeIndex.ZERO_ZERO.getSign(),TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        proxyRequestService.proxyCmd(deviceIp,bytes);
    }

    @Override
    public void getForwardCarbonEmission(Integer deviceId, Integer msgId, FrameDto frameDto) throws Exception {
        GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class, GetResponseNormalData.class);
        Object obj = parser.getData(parser.parseFrame(frameDto));
        ForwardCarbonEmission emission = new ForwardCarbonEmission();
        emission.setCarbonEmission((Integer)obj);
        emission.setDateTime(LocalDateTime.now());
        emission.setMsgId(msgId);
        emission.setDeviceId(deviceId);
        this.save(emission);
    }
}

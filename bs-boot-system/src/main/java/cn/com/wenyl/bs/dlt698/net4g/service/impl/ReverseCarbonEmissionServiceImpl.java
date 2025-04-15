package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.GetResponseNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.impl.FrameBuildProcessor;
import cn.com.wenyl.bs.dlt698.common.service.impl.GetRequestNormalFrameBuilder;
import cn.com.wenyl.bs.dlt698.common.service.impl.GetResponseNormalFrameParser;
import cn.com.wenyl.bs.dlt698.net4g.entity.ReverseCarbonEmission;
import cn.com.wenyl.bs.dlt698.net4g.mapper.ReverseCarbonEmissionMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.service.ReverseCarbonEmissionService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 反向碳排放 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class ReverseCarbonEmissionServiceImpl extends ServiceImpl<ReverseCarbonEmissionMapper, ReverseCarbonEmission> implements ReverseCarbonEmissionService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @Override
    public void getReverseCarbonEmission(String deviceIp) throws Exception{
        // 查询昨日碳排放累计量
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = FrameBuildUtils.getCommonFrame(GetRequestNormalFrame.class, FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS, LogicAddress.ZERO, carbonDeviceService.getDeviceAddress(deviceIp),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO, OI.REVERSE_CARBON_EMISSION, AttrNum.ATTR_02,AttributeIndex.ZERO_ZERO.getSign(),TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        deviceChannelManager.sendDataToDevice(deviceIp,bytes);
    }

    @Override
    public void getReverseCarbonEmission(Integer deviceId, Integer msgId, FrameDto frameDto) {
        GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class, GetResponseNormalData.class);
        Object obj = parser.getData(parser.parseFrame(frameDto));
        ReverseCarbonEmission emission = new ReverseCarbonEmission();
        emission.setCarbonEmission((Double)obj);
        emission.setDateTime(LocalDateTime.now());
        emission.setMsgId(msgId);
        emission.setDeviceId(deviceId);
        this.save(emission);
    }
}

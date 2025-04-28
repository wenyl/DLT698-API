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
import cn.com.wenyl.bs.dlt698.net4g.entity.ReverseActivePower;
import cn.com.wenyl.bs.dlt698.net4g.mapper.ReverseActivePowerMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.ReverseActivePowerService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 反向有功 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class ReverseActivePowerServiceImpl extends ServiceImpl<ReverseActivePowerMapper, ReverseActivePower> implements ReverseActivePowerService {
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
    public void getReverseActivePower(Integer deviceId, Integer msgId, FrameDto frameDto) throws Exception {
        try{
            GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class, GetResponseNormalData.class);
            GetResponseNormalFrame frame = parser.parseFrame(frameDto);

            if(frame.getNormalData().getDataType() == null || !frame.getNormalData().getDataType().equals(DataType.ARRAY)){
                throw new RuntimeException("反向有功电能查询返回数据类型异常异常!当前数据类型"+ frame.getNormalData().getDataType());
            }
            JSONArray array = (JSONArray) parser.getData(frame);
            ReverseActivePower power = new ReverseActivePower();
            power.setDateTime(LocalDateTime.now());
            power.setNow(((Long) array.get(0)) / (100.0));
            power.setSharp(((Long) array.get(1)) / (100.0));
            power.setPeak(((Long) array.get(2)) / (100.0));
            power.setShoulder(((Long) array.get(3)) / (100.0));
            power.setOffPeak(((Long) array.get(4)) / (100.0));
            power.setDeviceId(deviceId);
            power.setMsgId(msgId);
            this.save(power);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("反向有功电能查询返回数据异常!");
        }

    }

    @Override
    public void getReverseActivePower(String deviceIp) throws Exception {
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = FrameBuildUtils.getCommonFrame(GetRequestNormalFrame.class, FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS, LogicAddress.ZERO, carbonDeviceService.getDeviceAddress(deviceIp),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO, OI.RAEE, AttrNum.ATTR_02,AttributeIndex.ZERO_ZERO.getSign(),TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        proxyRequestService.proxyCmd(deviceIp,bytes);
    }
}

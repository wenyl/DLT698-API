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
import cn.com.wenyl.bs.dlt698.net4g.entity.ForwardActivePower;
import cn.com.wenyl.bs.dlt698.net4g.mapper.ForwardActivePowerMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.ForwardActivePowerService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 正向有功 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class ForwardActivePowerServiceImpl extends ServiceImpl<ForwardActivePowerMapper, ForwardActivePower> implements ForwardActivePowerService {
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
    public void getForwardActivePower(Integer deviceId, Integer msgId, FrameDto frameDto) throws Exception {
        try{
            GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class, GetResponseNormalData.class);
            GetResponseNormalFrame frame = parser.parseFrame(frameDto);
            if(frame.getNormalData().getDataType() == null || !frame.getNormalData().getDataType().equals(DataType.ARRAY)){
                throw new RuntimeException("正向有功电能查询返回数据异常!");
            }
            ForwardActivePower forwardActivePower = new ForwardActivePower();
            JSONArray array = (JSONArray) parser.getData(frame);
            forwardActivePower.setDateTime(LocalDateTime.now());
            forwardActivePower.setNow(((Long) array.get(0)) / (100.0));
            forwardActivePower.setSharp(((Long) array.get(1)) / (100.0));
            forwardActivePower.setPeak(((Long) array.get(2)) / (100.0));
            forwardActivePower.setShoulder(((Long) array.get(3)) / (100.0));
            forwardActivePower.setOffPeak(((Long) array.get(4)) / (100.0));
            forwardActivePower.setDeviceId(deviceId);
            forwardActivePower.setMsgId(msgId);
            this.save(forwardActivePower);
        }catch (Exception e){
            throw new RuntimeException("正向有功电能查询返回数据异常!");
        }

    }

    @Override
    public void getForwardActivePower(String deviceIp) throws Exception{
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = FrameBuildUtils.getCommonFrame(GetRequestNormalFrame.class, FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS, LogicAddress.ZERO, carbonDeviceService.getDeviceAddress(deviceIp),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO, OI.PAEE, AttrNum.ATTR_02,AttributeIndex.ZERO_ZERO.getSign(),TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        proxyRequestService.proxyCmd(deviceIp,bytes);
    }
}

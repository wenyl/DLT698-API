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
import cn.com.wenyl.bs.dlt698.net4g.entity.Electric;
import cn.com.wenyl.bs.dlt698.net4g.mapper.ElectricMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.service.ElectricService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备历史电流值 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class ElectricServiceImpl extends ServiceImpl<ElectricMapper, Electric> implements ElectricService {
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
    @Resource
    private DeviceMsgHisService deviceMsgHisService;
    @Override
    public void getElectricCurrent(String deviceIp) throws Exception{
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = FrameBuildUtils.getCommonFrame(GetRequestNormalFrame.class, FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS, LogicAddress.ZERO, carbonDeviceService.getDeviceAddress(deviceIp),
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO, OI.ELECTRIC_CURRENT, AttrNum.ATTR_02,AttributeIndex.ZERO_ONE.getSign(),TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        proxyRequestService.proxyCmd(deviceIp,bytes);
    }

    @Override
    public void getElectric(Integer deviceId, Integer msgId, FrameDto frameDto) throws Exception{
        try{
            GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class, GetResponseNormalData.class);
            GetResponseNormalFrame frame = parser.parseFrame(frameDto);
            if(frame.getNormalData().getDataType() == null || !frame.getNormalData().getDataType().equals(DataType.DOUBLE_LONG)){
                throw new RuntimeException("收到电流消息为数组,和预期数据不符");
            }
            Electric electric = new Electric();
            electric.setElectric((Integer)parser.getData(frame)/1000.0);
            electric.setDeviceId(deviceId);
            electric.setMsgId(msgId);
            electric.setDateTime(LocalDateTime.now());
            this.save(electric);
        }catch (Exception e){
            throw new RuntimeException("收到电流消息为数组,和预期数据不符");
        }

    }
}

package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.GetResponseNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.GetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.common.service.impl.FrameBuildProcessor;
import cn.com.wenyl.bs.dlt698.common.service.impl.GetRequestNormalFrameBuilder;
import cn.com.wenyl.bs.dlt698.common.service.impl.GetResponseNormalFrameParser;
import cn.com.wenyl.bs.dlt698.net4g.entity.Voltage;
import cn.com.wenyl.bs.dlt698.net4g.mapper.VoltageMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.service.VoltageService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.net4g.tcp.TcpServerHandler;
import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 设备电压历史值 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class VoltageServiceImpl extends ServiceImpl<VoltageMapper, Voltage> implements VoltageService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Override
    public void getVoltage(String deviceIp) throws Exception{
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = FrameBuildUtils.getCommonFrame(GetRequestNormalFrame.class, FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS, LogicAddress.ZERO, Address.COMMON_DEVICE_ADDRESS,
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO, OI.VOLTAGE, AttrNum.ATTR_02,AttributeIndex.ZERO_ZERO.getSign(),TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        deviceChannelManager.sendDataToDevice(deviceIp,bytes);
    }
    @Override
    public void getVoltage(Integer deviceId,Integer msgId,FrameDto frameDto) throws Exception{
        try{
            Voltage voltage = new Voltage();
            GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class, GetResponseNormalData.class);
            GetResponseNormalFrame frame = parser.parseFrame(frameDto);
            if(frame.getNormalData().getDataType() == null || !frame.getNormalData().getDataType().equals(DataType.ARRAY)){
                throw new RuntimeException("分项电压查询返回数据异常!");
            }
            JSONArray array = (JSONArray) parser.getData(frame);
            voltage.setVoltageA(((Integer) array.get(0)) / (10.0));
            voltage.setVoltageB(((Integer) array.get(1)) / (10.0));
            voltage.setVoltageC(((Integer) array.get(2)) / (10.0));
            voltage.setDeviceId(deviceId);
            voltage.setMsgId(msgId);
            voltage.setDateTime(LocalDateTime.now());
            this.save(voltage);
        }catch (Exception e){
            throw new RuntimeException("分项电压查询返回数据异常!");
        }


    }
}

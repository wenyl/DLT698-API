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
import cn.com.wenyl.bs.dlt698.net4g.entity.CarbonDevice;
import cn.com.wenyl.bs.dlt698.net4g.mapper.CarbonDeviceMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 碳表信息 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Slf4j
@Service
public class CarbonDeviceServiceImpl extends ServiceImpl<CarbonDeviceMapper, CarbonDevice> implements CarbonDeviceService {
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Resource
    @Lazy
    private ProxyRequestService proxyRequestService;
    private static final byte[] TEST_ADDRESS = {(byte)0x01,(byte)0x00,(byte)0x20,(byte)0x03,(byte)0x25,(byte)0x01};
    @Override
    public void isAlive(Integer deviceId) {
        LambdaUpdateWrapper<CarbonDevice> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(CarbonDevice::getDeviceStatus,1);
        updateWrapper.eq(CarbonDevice::getId,deviceId);
        this.update(updateWrapper);
    }

    @Override
    public void isDead(Integer deviceId) {
        LambdaUpdateWrapper<CarbonDevice> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(CarbonDevice::getDeviceStatus,0);
        updateWrapper.eq(CarbonDevice::getId,deviceId);
        this.update(updateWrapper);
    }

    @Override
    public void getAddress(String deviceIp) throws Exception{
        log.info("查询设备地址");
        GetRequestNormalFrameBuilder builder = (GetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(GetRequestNormalFrame.class);

        GetRequestNormalFrame getRequestNormalFrame = FrameBuildUtils.getCommonFrame(GetRequestNormalFrame.class,FunctionCode.THREE,ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.DISTRIBUTION_ADDRESS, LogicAddress.ZERO, Address.DISTRIBUTION_ADDRESS,
                Address.CLIENT_ADDRESS);

        GetRequestNormalData userData = new GetRequestNormalData(PIID.ZERO_ZERO, OI.MAIL_ADDRESS, AttrNum.ATTR_02,AttributeIndex.ZERO_ZERO.getSign(),TimeTag.NO_TIME_TAG);
        getRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(getRequestNormalFrame);
        proxyRequestService.proxyCmd(deviceIp,bytes);

    }

    @Override
    public void getAddress(String deviceIp, FrameDto frameDto) throws Exception {
        GetResponseNormalFrameParser parser = (GetResponseNormalFrameParser)frameParseProcessor.getFrameParser(GetResponseNormalFrame.class, GetResponseNormalData.class);
        String address = (String)parser.getData(parser.parseFrame(frameDto));
        byte[] bytes = HexUtils.hexStringToBytes(address);
        byte[] addressBytes = new byte[bytes.length];
        for(int i=0,j=bytes.length-1;i<addressBytes.length;i++){
            addressBytes[i] = bytes[j];
            j--;
        }
        String reverseAddress = HexUtils.bytesToHex(addressBytes);
        LambdaQueryWrapper<CarbonDevice> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CarbonDevice::getDeviceIp,deviceIp);
        CarbonDevice one = this.getOne(queryWrapper);
        one.setAddress(reverseAddress);
        deviceChannelManager.setDeviceAddress(deviceIp,reverseAddress);
        this.updateById(one);
    }

    @Override
    public byte[] getDeviceAddress(String deviceIp){
        byte[] address = deviceChannelManager.getDeviceAddress(deviceIp);
        if(address != null){
            return address;
        }
        LambdaQueryWrapper<CarbonDevice> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CarbonDevice::getDeviceIp,deviceIp);
        CarbonDevice one = this.getOne(queryWrapper);
        if(one == null || StringUtils.isBlank(one.getAddress())){
            throw new RuntimeException("设备地址不存在,请先查询设备地址,设备IP="+deviceIp);
        }

        return HexUtils.hexStringToBytes(one.getAddress());
    }
}

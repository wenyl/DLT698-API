package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.SetRequestNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.SetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.SetResponseNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.SetResponseNormalFrame;
import cn.com.wenyl.bs.dlt698.common.entity.dto.CarbonFactorDto;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.ProxyRequestService;
import cn.com.wenyl.bs.dlt698.common.service.impl.FrameBuildProcessor;
import cn.com.wenyl.bs.dlt698.common.service.impl.SetRequestNormalFrameBuilder;
import cn.com.wenyl.bs.dlt698.common.service.impl.SetResponseNormalFrameParser;
import cn.com.wenyl.bs.dlt698.net4g.entity.CarbonFactor;
import cn.com.wenyl.bs.dlt698.net4g.mapper.CarbonFactorMapper;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonFactorService;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.ASN1EncoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 电碳因子 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Service
public class CarbonFactorServiceImpl extends ServiceImpl<CarbonFactorMapper, CarbonFactor> implements CarbonFactorService {
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
    public void setCarbonFactor(String deviceIp, Double carbonFactor) throws Exception{
        SetRequestNormalFrameBuilder builder = (SetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(SetRequestNormalFrame.class);

        SetRequestNormalFrame setRequestNormalFrame = FrameBuildUtils.getCommonFrame(SetRequestNormalFrame.class, FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS, LogicAddress.ZERO, carbonDeviceService.getDeviceAddress(deviceIp),
                Address.CLIENT_ADDRESS);
        // todo 生成要设置的信息
        byte[] data = buildSetCarbonFactorBytes(carbonFactor);
        SetRequestNormalData userData = new SetRequestNormalData(data, PIID.ZERO_ZERO, OI.SET_CARBON_FACTOR, AttrNum.ATTR_02,AttributeIndex.ZERO_ZERO.getSign(),TimeTag.NO_TIME_TAG);
        setRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(setRequestNormalFrame);
        proxyRequestService.setProxyCmd(deviceIp,bytes,String.valueOf(carbonFactor));
    }

    @Override
    public void setCarbonFactors(CarbonFactorDto carbonFactorDto) throws Exception{
        SetRequestNormalFrameBuilder builder = (SetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(SetRequestNormalFrame.class);

        SetRequestNormalFrame setRequestNormalFrame = FrameBuildUtils.getCommonFrame(SetRequestNormalFrame.class,FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, carbonDeviceService.getDeviceAddress(carbonFactorDto.getDeviceIp()),
                Address.CLIENT_ADDRESS);
        // todo 生成要设置的信息
        byte[] data = buildSetCarbonFactorBytes(carbonFactorDto.getCarbonFactor());
        SetRequestNormalData userData = new SetRequestNormalData(data,PIID.ZERO_ZERO,OI.SET_CARBON_FACTOR, AttrNum.ATTR_02,AttributeIndex.ZERO_ZERO.getSign(),TimeTag.NO_TIME_TAG);
        setRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(setRequestNormalFrame);
        proxyRequestService.setProxyCmd(carbonFactorDto.getDeviceIp(),bytes,Arrays.toString(carbonFactorDto.getCarbonFactor()));
    }

    @Override
    public void getSetResult(Integer deviceId, Integer msgId, FrameDto frameDto) {
        try{
            SetResponseNormalFrameParser parser = (SetResponseNormalFrameParser)frameParseProcessor.getFrameParser(SetResponseNormalFrame.class, SetResponseNormalData.class);
            parser.getData(parser.parseFrame(frameDto));
        }catch (Exception e){
            throw new RuntimeException("解析设置碳因子帧数据失败!");
        }
    }

    private byte[] buildSetCarbonFactorBytes(Double carbonFactor){
        if(carbonFactor == null){
            log.error("请输入电碳因子");
            throw new RuntimeException("请输入电碳因子");
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        byteBuffer.put(DataType.STRUCTURE.getSign());
        byteBuffer.put((byte)0x03);// 结构体元素个数
        // 第一个元素为时间，长度7个字节
        byteBuffer.put(DataType.DATE_TIME_S.getSign());
        // 获取当天日期，并设置时间为 00:00:00 转换为 Date
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 编码时间
        byte[] dateTimeSBytes = ASN1EncoderUtils.encodeDateTimeS(date);
        byteBuffer.put(dateTimeSBytes);
        // 第二个元素为时间间隔，时间单位占据一个字节，时间间隔值类型为long-unsigned占据两个字节
        byteBuffer.put(DataType.TI.getSign());
        byteBuffer.put(TimeUnit.DAY.getSign());
        byteBuffer.put(TimeInterval.ONE_HOUR.getSign());// 时间间隔为1
        // 第三个元素为数组，存储了设置的值
        byteBuffer.put(DataType.ARRAY.getSign());
        byteBuffer.put((byte)0x01);// 数组长度为1
        byteBuffer.put(DataType.LONG_UNSIGNED.getSign());
        byteBuffer.put(ASN1EncoderUtils.encodeLongUnsigned(cover2Int(carbonFactor)));

        byte[] byteArray = new byte[byteBuffer.position()];
        byteBuffer.rewind(); // 重置位置到0
        byteBuffer.get(byteArray); // 从缓冲区读取到数组中
        return byteArray;
    }
    private byte[] buildSetCarbonFactorBytes(double[] carbonFactor){
        if(carbonFactor == null){
            log.error("请输入电碳因子");
            throw new RuntimeException("请输入电碳因子");
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(carbonFactor.length*3+32);
        byteBuffer.put(DataType.STRUCTURE.getSign());
        byteBuffer.put((byte)0x03);// 结构体元素个数
        // 第一个元素为时间，长度7个字节
        byteBuffer.put(DataType.DATE_TIME_S.getSign());
        // 获取当天日期，并设置时间为 00:00:00 转换为 Date
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 编码时间
        byte[] dateTimeSBytes = ASN1EncoderUtils.encodeDateTimeS(date);
        byteBuffer.put(dateTimeSBytes);
        // 第二个元素为时间间隔，时间单位占据一个字节，时间间隔值类型为long-unsigned占据两个字节
        byteBuffer.put(DataType.TI.getSign());
        byteBuffer.put(TimeUnit.MINUTE.getSign());
        byteBuffer.put(TimeInterval.FIFTEEN_MINUTE.getSign());// 时间间隔为1
        // 第三个元素为数组，存储了设置的值
        byteBuffer.put(DataType.ARRAY.getSign());
        byteBuffer.put((byte)(carbonFactor.length & 0xFF));// 数组长度为1
        for (double v : carbonFactor) {
            byteBuffer.put(DataType.LONG_UNSIGNED.getSign());
            byteBuffer.put(ASN1EncoderUtils.encodeLongUnsigned(cover2Int(v)));
        }
        byte[] byteArray = new byte[byteBuffer.position()];
        byteBuffer.rewind(); // 重置位置到0
        byteBuffer.get(byteArray); // 从缓冲区读取到数组中
        return byteArray;
    }
    private int cover2Int(Double val){
        // 计算小数的位数（避免精度损失）
        String str = String.valueOf(val);
        int decimalPlaces = str.contains(".") ? str.length() - str.indexOf(".") - 1 : 0;

        // 根据小数位数扩大相应倍数
        int multiplier = (int) Math.pow(10, decimalPlaces);

        // 返回放大后的整数
        return (int) (val * multiplier);
    }
}

package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.*;
import cn.com.wenyl.bs.dlt698.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.service.CarbonFactorService;
import cn.com.wenyl.bs.dlt698.service.RS485Service;
import cn.com.wenyl.bs.dlt698.utils.ASN1EncoderUtils;
import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
@Slf4j
@Service
public class CarbonFactorServiceImpl implements CarbonFactorService {
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @Resource
    private FrameBuildProcessor frameBuildProcessor;
    @Resource
    private FrameParseProcessor frameParseProcessor;
    @Resource
    private RS485Service rs485Service;
    @Override
    public Object set1CarbonFactor(String carbonDeviceAddress,Double carbonFactor)  throws ExecutionException, InterruptedException, TimeoutException {
        // 先链接电表
        carbonDeviceService.connectCarbonDevice(carbonDeviceAddress);
        // 查询昨日碳排放累计量
        SetRequestNormalFrameBuilder builder = (SetRequestNormalFrameBuilder)frameBuildProcessor.getFrameBuilder(SetRequestNormalFrame.class);

        SetRequestNormalFrame setRequestNormalFrame = (SetRequestNormalFrame)builder.getFrame(FunctionCode.THREE, ScramblingCodeFlag.NOT_SCRAMBLING_CODE, FrameFlag.NOT_SUB_FRAME,
                RequestType.CLIENT_REQUEST, AddressType.SINGLE_ADDRESS,LogicAddress.ZERO, BCDUtils.encodeBCD(carbonDeviceAddress),
                Address.CLIENT_ADDRESS);
        // todo 生成要设置的信息
        byte[] data = buildSetCarbonFactorBytes(carbonFactor);
        SetRequestNormalData userData = new SetRequestNormalData(data,PIID.ZERO_ZERO,OI.SET_CARBON_FACTOR, AttrNum.ATTR_02,AttributeIndex.ZERO,TimeTag.NO_TIME_TAG);
        setRequestNormalFrame.setData(userData);
        byte[] bytes = builder.buildFrame(setRequestNormalFrame);
        log.info(HexUtils.bytesToHex(bytes));
        try{
            byte[] returnFrame = rs485Service.sendByte(bytes);
            log.info("收到数据帧{}", HexUtils.bytesToHex(returnFrame));

            SetResponseNormalFrameParser parser = (SetResponseNormalFrameParser)frameParseProcessor.getFrameParser(SetResponseNormalFrame.class);
            return parser.getData(parser.parseFrame(returnFrame));
        } finally{
            SerialCommUtils.getInstance().closePort();
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
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT);
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 编码时间
        byte[] dateTimeSBytes = ASN1EncoderUtils.encodeDateTimeS(date);
        byteBuffer.put(dateTimeSBytes);
        // 第二个元素为时间间隔，时间单位占据一个字节，时间间隔值类型为long-unsigned占据两个字节
        byteBuffer.put(DataType.TI.getSign());
        byteBuffer.put(TimeUnit.DAY.getSign());
        byteBuffer.put(new byte[]{(byte)0x00,(byte)0x01});// 时间间隔为1
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

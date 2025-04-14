package cn.com.wenyl.bs.dlt698.net4g.tcp;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.net4g.entity.CarbonDevice;
import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceErrorMsgHis;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceErrorMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

@Slf4j
@Component
@Scope("prototype") // 每个连接使用新的 handler 实例（很重要！）
public class TcpServerHandler  extends SimpleChannelInboundHandler<ByteBuf> {
    @Resource
    private FrameParseService frameParseService;
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Resource
    private DeviceMsgHisService deviceMsgHisService;
    @Resource
    private DeviceErrorMsgHisService deviceErrorMsgHisService;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String deviceIp = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        // 数据库不存在则保存这个数据
        LambdaQueryWrapper<CarbonDevice> query = new LambdaQueryWrapper<>();
        query.eq(CarbonDevice::getDeviceIp,deviceIp);
        CarbonDevice carbonDevice = carbonDeviceService.getOne(query);
        CarbonDevice newDevice = new CarbonDevice();
        int deviceId;
        if(carbonDevice == null){
            newDevice.setDeviceIp(deviceIp);
            newDevice.setCreateTime(LocalDateTime.now());
            carbonDeviceService.save(newDevice);
            deviceId = newDevice.getId();
        }else{
            deviceId = carbonDevice.getId();
        }
        deviceChannelManager.addDevice(deviceId,deviceIp, ctx.channel());
        log.info("设备链接:id={},ip={},port={}",deviceId,deviceIp,port);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String deviceIp = socketAddress.getAddress().getHostAddress();
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        log.info("收到设备数据:{}",HexUtils.bytesToHex(bytes));
        if(!FrameParseUtils.checkFrame(bytes)){
            String errorInfo = "无效帧：起始符或结束符错误,当前帧起始符--"+HexUtils.byteToHex(bytes[0])+",结束符--"+HexUtils.byteToHex(bytes[bytes.length-1]);
            log.error(errorInfo);
            return;
        }
        Integer deviceId = deviceChannelManager.getDeviceId(deviceIp);
        if(deviceId == null){
            log.error("未知设备ip={}",deviceIp);
            return;
        }
        FrameDto frameDto;
        try{
            frameDto = FrameParseUtils.getFrameDto(bytes);
        }catch (Exception e){
            DeviceErrorMsgHis errorMsgHis = new DeviceErrorMsgHis();
            errorMsgHis.setByteData(HexUtils.bytesToHex(bytes));
            errorMsgHis.setDataLength(bytes.length);
            errorMsgHis.setCreateTime(LocalDateTime.now());
            errorMsgHis.setErrorMsg(e.getMessage());
            deviceErrorMsgHisService.save(errorMsgHis);
            log.error("帧数据解析报错--{}",e.getMessage());
            return;
        }
        try {
            // 根据IP查询数据
            Integer msgId = deviceMsgHisService.save(frameDto,deviceId,bytes);
            frameParseService.frameParse(msgId,frameDto,deviceIp,bytes);
        } catch (Exception e) {
            DeviceErrorMsgHis errorMsgHis = new DeviceErrorMsgHis();
            errorMsgHis.setByteData(HexUtils.bytesToHex(bytes));
            errorMsgHis.setDataLength(bytes.length);
            errorMsgHis.setCreateTime(LocalDateTime.now());
            errorMsgHis.setErrorMsg(e.getMessage());
            deviceErrorMsgHisService.save(errorMsgHis);
            log.error("帧数据保存解析解析报错--{}",e.getMessage());
            log.error("解析收到的帧数据异常，设备ID: {}, 字节内容: {}", deviceIp, HexUtils.bytesToHex(bytes), e);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String deviceIp = ctx.channel().remoteAddress().toString();
        deviceChannelManager.removeDevice(deviceIp);
        log.info("设备断开:{}",deviceIp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("TCP 服务器启动失败",cause.getCause());
        ctx.close();
    }
}

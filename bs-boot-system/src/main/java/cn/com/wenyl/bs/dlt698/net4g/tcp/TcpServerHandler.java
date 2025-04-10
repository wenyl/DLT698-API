package cn.com.wenyl.bs.dlt698.net4g.tcp;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.net4g.entity.CarbonDevice;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.service.StationDeviceMsgRelaService;
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String deviceIp = ctx.channel().remoteAddress().toString();

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
        log.info("设备链接:id={},ip={}",deviceId,deviceIp);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        String deviceIp = ctx.channel().remoteAddress().toString();

        byte[] bytes = new byte[msg.readableBytes()];
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
        // 根据IP查询数据
        FrameDto frameDto = FrameParseUtils.getFrameDto(bytes);
        msg.readBytes(bytes);
        log.info("收到设备数据:{}",HexUtils.bytesToHex(bytes));
        try {
            deviceMsgHisService.save(frameDto,deviceId,bytes);
            frameParseService.frameParse(frameDto,deviceIp,bytes);
        } catch (Exception e) {
            log.error("解析收到的帧数据异常，设备ID: {}, 字节内容: {}, 错误信息: {}", deviceIp, HexUtils.bytesToHex(bytes), e.getMessage());
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

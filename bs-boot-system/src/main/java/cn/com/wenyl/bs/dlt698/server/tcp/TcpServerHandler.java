package cn.com.wenyl.bs.dlt698.server.tcp;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.server.annotation.FrameLog;
import cn.com.wenyl.bs.dlt698.server.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

@Slf4j
@Component
@Scope("prototype") // 每个连接使用新的 handler 实例（很重要！）
public class TcpServerHandler  extends SimpleChannelInboundHandler<ByteBuf> {
    @Resource
    @Lazy
    private FrameParseService frameParseService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String deviceId = ctx.channel().remoteAddress().toString();
        // todo 这里要判断数据库中是否已经存在这个设备
        DeviceChannelManager.addDevice(deviceId, ctx.channel());
        log.info("设备链接:{}",deviceId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        String deviceId = ctx.channel().remoteAddress().toString();
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        log.info("收到设备数据:{}",HexUtils.bytesToHex(bytes));
        try {
            frameParseService.frameParse(deviceId,bytes);
        } catch (Exception e) {
            log.error("解析收到的帧数据异常，设备ID: {}, 字节内容: {}, 错误信息: {}", deviceId, HexUtils.bytesToHex(bytes), e.getMessage());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String deviceId = ctx.channel().remoteAddress().toString();
        DeviceChannelManager.removeDevice(deviceId);
        log.info("设备断开:{}",deviceId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("TCP 服务器启动失败",cause.getCause());
        ctx.close();
    }

    /**
     * 向设备发送数据
     * @param deviceId 设备ID
     * @param data 数据
     */
    public void sendDataToDevice(String deviceId, byte[] data) {
        Channel deviceChannel = DeviceChannelManager.getDeviceChannel(deviceId);
        if (deviceChannel != null) {
            deviceChannel.writeAndFlush(Unpooled.copiedBuffer(data));
            log.info("向设备 {} 发送数据: {}", deviceId, HexUtils.bytesToHex(data));
        } else {
            log.error("设备 {} 未连接，无法发送数据", deviceId);
        }
    }
}

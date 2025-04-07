package cn.com.wenyl.bs.dlt698.server.tcp;

import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class TcpServerHandler  extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String deviceId = ctx.channel().remoteAddress().toString();
        // todo 这里要判断数据库中是否已经存在这个设备
        DeviceChannelManager.addDevice(deviceId, ctx.channel());
        ctx.writeAndFlush(Unpooled.copiedBuffer("ACK\n".getBytes(StandardCharsets.UTF_8)));
        log.info("设备链接:{}",deviceId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        log.info("收到设备数据:{}",HexUtils.bytesToHex(bytes));
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
}

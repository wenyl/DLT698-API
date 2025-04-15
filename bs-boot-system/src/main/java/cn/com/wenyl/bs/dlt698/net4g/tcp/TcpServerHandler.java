package cn.com.wenyl.bs.dlt698.net4g.tcp;

import cn.com.wenyl.bs.dlt698.common.constants.Address;
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
import org.apache.commons.lang.StringUtils;
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
        try{
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
                deviceChannelManager.addDevice(deviceId,deviceIp, ctx.channel());
                deviceChannelManager.setDeviceAddress(deviceIp, HexUtils.bytesToHex(Address.SERVER_ADDRESS));
                // 这里还要去查询碳表地址
//                carbonDeviceService.getAddress(deviceIp);
            }else{
                deviceId = carbonDevice.getId();
                deviceChannelManager.addDevice(deviceId,deviceIp, ctx.channel());
                if(StringUtils.isEmpty(carbonDevice.getAddress())){
                    throw new RuntimeException("碳表未设置地址信息");
                }else{
                    deviceChannelManager.setDeviceAddress(deviceIp,carbonDevice.getAddress());
                }

            }
            log.info("设备链接:id={},ip={},port={}",deviceId,deviceIp,port);
        }catch (Exception e){
            log.error("碳表信息初始化错误,{}",e.getMessage());
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String deviceIp = socketAddress.getAddress().getHostAddress();
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        log.info("收到设备数据:{}",HexUtils.bytesToHex(bytes));
        try{
            frameParseService.parseBytes(deviceIp,bytes);
        }catch (Exception e){
            log.error(e.getMessage());
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

package cn.com.wenyl.bs.dlt698.net4g.tcp;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceErrorMsgHis;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceErrorMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
public class DeviceChannelManager {
    @Resource
    @Lazy
    private CarbonDeviceService carbonDeviceService;
    @Resource
    private DeviceMsgHisService deviceMsgHisService;
    @Resource
    private DeviceErrorMsgHisService deviceErrorMsgHisService;
    // 存储设备的连接信息（key: 设备ID 或 设备IP，value: Channel）
    private final ConcurrentHashMap<String, Channel> deviceChannels = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,Integer> deviceIds = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,String> deviceAddress = new ConcurrentHashMap<>();
    // 添加设备连接
    public void addDevice(int deviceId,String deviceIp, Channel channel) {
        deviceChannels.put(deviceIp, channel);
        deviceIds.put(deviceIp,deviceId);
    }

    // 添加设备地址
    public void setDeviceAddress(String deviceIp,String address){
        deviceAddress.put(deviceIp,address);
    }

    public byte[] getDeviceAddress(String deviceIp){
        String address = deviceAddress.get(deviceIp);
        return HexUtils.hexStringToBytes(address);
    }
    // 移除设备连接
    public void removeDevice(String deviceIp) {
        deviceChannels.remove(deviceIp);
        deviceIds.remove(deviceIp);
        deviceAddress.remove(deviceIp);
        isDead(deviceIp);
    }

    // 获取设备的 Channel
    public Channel getDeviceChannel(String deviceIp) {
        return deviceChannels.get(deviceIp);
    }
    // 获取设备IP
    public Integer getDeviceId(String deviceIp){return deviceIds.get(deviceIp);}
    /**
     * 向设备发送数据
     * @param deviceIp 设备IP
     * @param data 数据
     */
    public void sendDataToDevice(String deviceIp, byte[] data) throws RuntimeException{
        FrameDto frameDto;
        Integer deviceId = this.getDeviceId(deviceIp);
        try{
            frameDto = FrameParseUtils.getFrameDto(data);
            deviceMsgHisService.save(frameDto,deviceId,data);
            Channel deviceChannel = getDeviceChannel(deviceIp);
            if (deviceChannel != null) {
                deviceChannel.writeAndFlush(Unpooled.copiedBuffer(data));
                log.info("向设备 {} 发送数据: {}", deviceIp, HexUtils.bytesToHex(data));
            } else {
                log.error("设备 {} 未连接，无法发送数据", deviceIp);
            }
        } catch (Exception e) {
            DeviceErrorMsgHis errorMsgHis = new DeviceErrorMsgHis();
            errorMsgHis.setDeviceId(deviceId);
            errorMsgHis.setByteData(HexUtils.bytesToHex(data));
            errorMsgHis.setDataLength(data.length);
            errorMsgHis.setCreateTime(LocalDateTime.now());
            errorMsgHis.setErrorMsg(e.getMessage());
            deviceErrorMsgHisService.save(errorMsgHis);
            log.error("帧数据解析报错--{}",e.getMessage());
            throw new RuntimeException(e);
        }


    }

    /**
     * 设备在线
     * @param deviceIp 设备IP
     */
    public void isAlive(String deviceIp){
        Integer deviceId = getDeviceId(deviceIp);
        carbonDeviceService.isAlive(deviceId);
    }

    /**
     * 设备离线
     * @param deviceIp 设备IP
     */
    public void isDead(String deviceIp){
        Integer deviceId = getDeviceId(deviceIp);
        carbonDeviceService.isDead(deviceId);
    }
}

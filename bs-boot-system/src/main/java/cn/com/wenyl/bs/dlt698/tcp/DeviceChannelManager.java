package cn.com.wenyl.bs.dlt698.tcp;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class DeviceChannelManager {
    // 存储设备的连接信息（key: 设备ID 或 设备IP，value: Channel）
    private static final ConcurrentHashMap<String, Channel> deviceChannels = new ConcurrentHashMap<>();

    // 添加设备连接
    public static void addDevice(String deviceId, Channel channel) {
        deviceChannels.put(deviceId, channel);
    }

    // 移除设备连接
    public static void removeDevice(String deviceId) {
        deviceChannels.remove(deviceId);
    }

    // 获取设备的 Channel
    public static Channel getDeviceChannel(String deviceId) {
        return deviceChannels.get(deviceId);
    }

    // 向指定设备发送消息
    public static boolean sendMessageToDevice(String deviceId, String message) {
        Channel channel = deviceChannels.get(deviceId);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message + "\n");
            return true;
        }
        return false; // 设备未连接
    }
}

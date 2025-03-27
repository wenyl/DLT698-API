package cn.com.wenyl.bs.dlt698.annotation;

import lombok.Data;

@Data
public class DeviceOperateContext {
    private static final ThreadLocal<DeviceOperateContext> contextHolder = ThreadLocal.withInitial(DeviceOperateContext::new);
    private Long jobId; // 任务ID
    private String sentFrame; // 发送的数据帧
    private String receivedFrame; // 接收的数据帧
    private String valueJson; // 解析的数据（JSON 格式）
    private boolean isRootMethod = false; // 是否为根方法（主方法）
    public static DeviceOperateContext get() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }

}

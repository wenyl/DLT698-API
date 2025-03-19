package cn.com.wenyl.bs.dlt698.entity;

/**
 * 链接信息
 */
public class AppConnectInfo {
    // 期望的协议版本号
    private byte[] protocolVersion;
    // 期望的协议一致性块
    private byte[] protocolConformance;
    // 期望的功能一致性块
    private byte[] functionConformance;
    // 客户机发送帧最大尺寸——单位：字节 long-unsigned
    private byte[] maxSendPerFrame;
    // 客户机接收帧最大尺寸——单位：字节 long-unsigned
    private byte[] maxReceivePerFrame;
    // 客户机接收帧最大窗口尺寸——单位：个 unsigned
    private byte[] maxFrameNum;
    // 客户机最大可处理帧尺寸——单位：字节 long-unsigned
    private byte[] maxAPDUSize;
    // 期望的应用连接超时时间  double-long-unsigned
    private byte[] connectTimeout;
    // 认证请求对象
    private byte[] connectMechanismInfo;
}

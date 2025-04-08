package cn.com.wenyl.bs.dlt698.common.entity.dto;

import lombok.Data;

@Data
public class ConnectInfoDto {

    // 期望的协议版本号
    private byte[] protocolVersion;
    // 期望的协议一致性块
    private byte[] protocolConformance ;
    // 期望的功能一致性块
    private byte[] functionConformance ;
    // 客户机发送帧最大尺寸——单位：字节 long-unsigned
    private byte[] maxSendPerFrame;
    // 客户机接收帧最大尺寸——单位：字节 long-unsigned
    private byte[] maxReceivePerFrame;
    // 客户机接收帧最大窗口尺寸——单位：个 unsigned
    private byte[] maxFrameNum;
    // 客户机最大可处理帧尺寸——单位：字节 long-unsigned
    private byte[] maxApduSize;
    // 期望的应用连接超时时间  double-long-unsigned
    private byte[] connectTimeout;
    // 认证请求对象
    private byte connectMechanismInfo;
    // 请求认证的机制信息 （公共链接0 一般密码1 对称加密2 数字签名3）
    private byte securityType;
}

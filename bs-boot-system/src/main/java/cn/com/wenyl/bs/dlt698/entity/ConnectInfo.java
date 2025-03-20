package cn.com.wenyl.bs.dlt698.entity;

import lombok.Data;

public class ConnectInfo {
    // 期望的协议版本号
    public static final byte[] PROTOCOL_VERSION = {(byte)0x00,(byte)0x16};
    // 期望的协议一致性块
    public static final byte[] PROTOCOL_CONFORMANCE = {(byte)0xff ,(byte)0xff ,(byte)0xff ,(byte)0xff ,(byte)0xc0 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00};
    // 期望的功能一致性块
    public static final byte[] FUNCTION_CONFORMANCE = {(byte)0xff ,(byte)0xfe ,(byte)0xc4 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00 ,(byte)0x00};
    // 客户机发送帧最大尺寸——单位：字节 long-unsigned
    public static final byte[] MAX_SEND_PER_FRAME = { (byte)0x02  ,(byte)0x00};
    // 客户机接收帧最大尺寸——单位：字节 long-unsigned
    public static final byte[] MAX_RECEIVE_PER_FRAME = {(byte)0x00 ,(byte)0xc8};
    // 客户机接收帧最大窗口尺寸——单位：个 unsigned
    public static final byte[] MAX_FRAME_NUM = {(byte)0x01};
    // 客户机最大可处理帧尺寸——单位：字节 long-unsigned
    public static final byte[] MAX_APDU_SIZE = {(byte)0x07 ,(byte)0xd0};
    // 期望的应用连接超时时间  double-long-unsigned
    public static final byte[] CONNECT_TIMEOUT = {(byte)0x00 ,(byte)0x00 ,(byte)0x1c ,(byte)0x20};
    // 认证请求对象
    public static final byte CONNECT_MECHANISM_INFO = (byte)0x00;
    // 请求认证的机制信息 （公共链接0 一般密码1 对称加密2 数字签名3）
    public static final byte SECURITY_TYPE = (byte)0x00;


}

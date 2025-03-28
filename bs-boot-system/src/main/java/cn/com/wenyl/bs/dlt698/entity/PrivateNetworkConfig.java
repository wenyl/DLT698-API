package cn.com.wenyl.bs.dlt698.entity;

import lombok.Data;

/**
 * 专网通信参数
 */
public class PrivateNetworkConfig {

    /**
     * 逻辑名(octet=string)
     */
    private byte[] logicName;

    /**
     * 通信配置(structure)
     */
    private byte[] commConfig;
}
@Data
class CommConfig{
    /**
     * 工作模式
     * enum{混合模式（0），客户机模式（1），服务器模式（2）}
     */
    private byte workMode;

    /**
     * 在线方式
     * enum{永久在线（0），被动激活（1）}
     */
    private byte onlineMode;

    /**
     * 连接方式
     * enum{TCP（0），UDP（1）}，
     */
    private byte connMode;

    /**
     * 连接应用方式
     * enum{主备模式（0），多连接模式（1）}
     */
    private byte connAppMode;

    /**
     * 侦听端口列表
     * array long-unsigned
     */
    private byte listenPortList;

    /**
     * APN
     * visible-string
     */
    private byte apn;

    /**
     * 用户名
     * visible-string
     */
    private byte username;

    /**
     * 密码
     */
    private byte password;

    /**
     * 代理服务器地址
     * octet-string
     */
    private byte proxyAddress;

    /**
     * 代理端口
     * long-unsigned
     */
    private byte proxyPort;

    /**
     * 超时时间及重发次数  unsigned
     * （
     * bit0„bit1：重发次数，
     * bit2„bit7：超时时间（秒）
     * ）
     */
    private byte timeout;

    /**
     * 心跳周期(秒)
     * long-unsigned
     */
    private byte heartbeatInterval;
}

/**
 * 主站通信参数表∷=array 主站通信参数
 * 主站通信参数∷=structure
 */
class MainStationComm{
    /**
     * IP地址
     * octet-string
     */
    private byte ipAddr;
    /**
     * 端口
     * long-unsigned
     */
    private byte port;
}


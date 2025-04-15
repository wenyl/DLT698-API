package cn.com.wenyl.bs.dlt698.common.constants;

/**
 * 透明转发报文常量
 */
public class ProxyRequestConst {
    /**
     * 端口通信控制块  波特率=9600(6),校验位=偶校验(2),数据位=8,停止位=1,流控=无(0)
     */
    public static final byte[] COM_DCB = {(byte)0x06,(byte)0x02,(byte)0x08,(byte)0x01,(byte)0x00};
    /**
     * 接收等待报文超时时间（秒） 10s
     */
    public static final byte[] USER_REV_TIMEOUT = {(byte)0x00,(byte)0x0A};

    /**
     * 接收等待字节超时时间（毫秒） 100ms
     */
    public static final byte[] USER_BYTE_TIMEOUT = {(byte)0x00,(byte)0x64};
}

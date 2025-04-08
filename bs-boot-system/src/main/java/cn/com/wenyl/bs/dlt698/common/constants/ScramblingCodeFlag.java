package cn.com.wenyl.bs.dlt698.common.constants;

/**
 * 扰码标志位: bit3=0，表示此帧链路用户数据不加扰码；bit3=1，表示此帧链路用户数据加扰码，
 * 发送时链路用户数据按字节加33H。
 */
public class ScramblingCodeFlag {
    // 加扰码
    public static final int SCRAMBLING_CODE = 1;
    // 不加扰码
    public static final int NOT_SCRAMBLING_CODE = 0;
}

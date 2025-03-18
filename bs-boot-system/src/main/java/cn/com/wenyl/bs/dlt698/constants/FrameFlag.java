package cn.com.wenyl.bs.dlt698.constants;

/**
 * 分帧标志位: bit5=0，表示此帧链路用户数据为完整APDU；bit5=1，表示此帧链路用户数据为APDU
 * 片段。
 */
public class FrameFlag {
    // 分帧 此帧链路用户数据为APDU片段
    public static final int SUB_FRAME = 1;
    // 此帧链路用户数据为完整APDU
    public static final int NOT_SUB_FRAME = 0;
}

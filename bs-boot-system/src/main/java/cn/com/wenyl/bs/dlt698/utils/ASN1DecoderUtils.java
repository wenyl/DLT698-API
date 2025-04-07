package cn.com.wenyl.bs.dlt698.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * ASN1编码工具类
 */
public class ASN1DecoderUtils {
    private static final String[]  DAY_OF_WEEK = {"天","一","二","三","四","五","六"};
    /**
     * 解码Long-Unsigned
     * @param data 数据
     * @return 值
     * @throws IllegalArgumentException 异常
     */
    public static int decodeLongUnsigned(byte[] data) throws IllegalArgumentException {
        if (data == null || data.length != 2) {
            throw new IllegalArgumentException("Invalid long-unsigned data (must be 2 bytes)");
        }

        return ((data[0] & 0xFF) << 8) | (data[1] & 0xFF);
    }

    /**
     * 解码DateTimeS，长度七个字节单位
     * @param dateTimeSBytes 时间
     * @return 时间字符串
     */
    public static String decodeDateTimeS(byte[] dateTimeSBytes) {
        if (dateTimeSBytes == null || dateTimeSBytes.length != 7) {
            throw new IllegalArgumentException("Invalid date_time_s format. Expected 7 bytes.");
        }

        int yearHigh = ((dateTimeSBytes[0] >> 4) & 0x0F) * 10 + (dateTimeSBytes[0] & 0x0F);
        int yearLow = ((dateTimeSBytes[1] >> 4) & 0x0F) * 10 + (dateTimeSBytes[1] & 0x0F);
        int year = yearHigh * 100 + yearLow;
        int month = ((dateTimeSBytes[2] >> 4) & 0x0F) * 10 + (dateTimeSBytes[2] & 0x0F);
        int day = ((dateTimeSBytes[3] >> 4) & 0x0F) * 10 + (dateTimeSBytes[3] & 0x0F);
        int hour = ((dateTimeSBytes[4] >> 4) & 0x0F) * 10 + (dateTimeSBytes[4] & 0x0F);
        int minute = ((dateTimeSBytes[5] >> 4) & 0x0F) * 10 + (dateTimeSBytes[5] & 0x0F);
        int second = ((dateTimeSBytes[6] >> 4) & 0x0F) * 10 + (dateTimeSBytes[6] & 0x0F);

        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }

    /**
     * 解释DateTime，长度10个字节
     * @param data 时间
     * @return 时间字符串
     */
    public static String decodeDateTime(byte[] data){
        if (data.length != 10) {
            throw new IllegalArgumentException("Invalid DATE-TIME length. Expected 10 bytes.");
        }

        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);

        int year = buffer.getShort() & 0xFFFF;
        int month = buffer.get() & 0xFF;
        int day = buffer.get() & 0xFF;
        int dayOfWeek = buffer.get() & 0xFF;
        int hour = buffer.get() & 0xFF;
        int minute = buffer.get() & 0xFF;
        int second = buffer.get() & 0xFF;
        int milliseconds = buffer.getShort() & 0xFFFF;

        return String.format("%04d-%02d-%02d 周%s %02d:%02d:%02d.%d", year, month, day, DAY_OF_WEEK[dayOfWeek],hour,  minute, second,milliseconds);
    }
}
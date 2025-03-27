package cn.com.wenyl.bs.dlt698.utils;

/**
 * ASN1编码工具类
 */
public class ASN1DecoderUtils {
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
     * @param dateTimeBytes 时间
     * @return 时间字节
     */
    public static String decodeDateTimeS(byte[] dateTimeBytes) {
        if (dateTimeBytes == null || dateTimeBytes.length != 7) {
            throw new IllegalArgumentException("Invalid date_time_s format. Expected 7 bytes.");
        }

        int yearHigh = ((dateTimeBytes[0] >> 4) & 0x0F) * 10 + (dateTimeBytes[0] & 0x0F);
        int yearLow = ((dateTimeBytes[1] >> 4) & 0x0F) * 10 + (dateTimeBytes[1] & 0x0F);
        int year = yearHigh * 100 + yearLow;
        int month = ((dateTimeBytes[2] >> 4) & 0x0F) * 10 + (dateTimeBytes[2] & 0x0F);
        int day = ((dateTimeBytes[3] >> 4) & 0x0F) * 10 + (dateTimeBytes[3] & 0x0F);
        int hour = ((dateTimeBytes[4] >> 4) & 0x0F) * 10 + (dateTimeBytes[4] & 0x0F);
        int minute = ((dateTimeBytes[5] >> 4) & 0x0F) * 10 + (dateTimeBytes[5] & 0x0F);
        int second = ((dateTimeBytes[6] >> 4) & 0x0F) * 10 + (dateTimeBytes[6] & 0x0F);

        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }
}
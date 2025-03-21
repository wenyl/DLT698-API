package cn.com.wenyl.bs.dlt698.utils;

import cn.com.wenyl.bs.dlt698.constants.DataType;

import java.util.Calendar;
import java.util.Date;

/**
 * ASN1编码工具类
 */
public class ASN1EncoderUtils {
    public static byte[] encodeLongUnsigned(int value) throws IllegalArgumentException {
        if (value < 0 || value > 0xFFFF) {
            throw new IllegalArgumentException("Value out of range for long-unsigned (0-65535)");
        }
        byte[] result = new byte[2];
        result[0] = (byte) ((value >> 8) & 0xFF); // 高字节
        result[1] = (byte) (value & 0xFF);        // 低字节
        return result;
    }

    /**
     * 编码时间，长度七个字节单位
     * @param date 时间
     * @return 时间字节
     */
    public static byte[] encodeDateTimeS(Date date) {
        byte[] dateTimeSBytes = new byte[DataType.DATE_TIME_S.getLength()];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);   // 获取年份
        int month = calendar.get(Calendar.MONTH) + 1; // 月份 (0-11 需+1)
        int day = calendar.get(Calendar.DAY_OF_MONTH); // 日
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // 年（大端模式）
        dateTimeSBytes[0] = (byte) ((year >> 8) & 0xFF);
        dateTimeSBytes[1] = (byte) (year & 0xFF);
        // 月
        dateTimeSBytes[2] = (byte) (month & 0xFF);
        // 日
        dateTimeSBytes[3] = (byte) (day & 0xFF);
        // 时
        dateTimeSBytes[4] = (byte) (hour & 0xFF);
        // 分
        dateTimeSBytes[5] = (byte) (minute & 0xFF);
        // 秒
        dateTimeSBytes[6] = (byte) (second & 0xFF);
        return dateTimeSBytes;
    }
}
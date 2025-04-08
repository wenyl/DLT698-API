package cn.com.wenyl.bs.dlt698.utils;

import cn.com.wenyl.bs.dlt698.common.constants.DataType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

    /**
     * 将当前时间转换为字节数组，长度为10个字节
     * @return 当前时间的字节数组
     */
    public static byte[] encodeDateTime() {
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();

        // 提取日期时间信息
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH 是从 0 开始的
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 1-7，表示星期天到星期六
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int milliseconds = calendar.get(Calendar.MILLISECOND);

        // 创建字节缓冲区，大小为10字节
        ByteBuffer buffer = ByteBuffer.allocate(10).order(ByteOrder.BIG_ENDIAN);

        // 按照指定顺序写入字节数组
        buffer.putShort((short) year); // 年
        buffer.put((byte) month); // 月
        buffer.put((byte) day); // 日
        buffer.put((byte) (dayOfWeek - 1)); // 星期（0-6，0表示星期日，1表示星期一）
        buffer.put((byte) hour); // 小时
        buffer.put((byte) minute); // 分钟
        buffer.put((byte) second); // 秒
        buffer.putShort((short) milliseconds); // 毫秒

        return buffer.array();
    }
}
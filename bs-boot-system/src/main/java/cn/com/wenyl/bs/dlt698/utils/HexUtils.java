package cn.com.wenyl.bs.dlt698.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 十六进制工具类
 */
@Slf4j
public class HexUtils {
    /**
     * 字节数组转十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X ", b));
        }
        return hex.toString().trim();
    }

    /**
     * 十六进制字符串转字节数组
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        // 移除空格并统一大写
        hexString = hexString.replaceAll(" ", "").toUpperCase();
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("十六进制字符串长度必须为偶数");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            String hex = hexString.substring(i, i + 2);
            bytes[i / 2] = (byte) Integer.parseInt(hex, 16);
        }
        return bytes;
    }

    /**
     * 字节转十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    public static String byteToHex(byte b) {
        return String.format("%02X", b);
    }


    /**
     * 解析二进制格式的 date_time_s
     * @param dateTimeBytes  字节数组
     * @return 返回时间
     */
    public static String parseDateTimes(byte[] dateTimeBytes) throws IllegalArgumentException{
        if (dateTimeBytes == null || dateTimeBytes.length != 7) {
            log.error("非法的 date_time_s 格式,期望长度为7个字节");
            throw new IllegalArgumentException("非法的 date_time_s 格式,期望长度为7个字节");
        }
        // 年（大端模式）
        int year = ((dateTimeBytes[0] & 0xFF) << 8) | (dateTimeBytes[1] & 0xFF);
        // 月
        int month = dateTimeBytes[2] & 0xFF;
        // 日
        int day = dateTimeBytes[3] & 0xFF;
        // 时
        int hour = dateTimeBytes[4] & 0xFF;
        // 分
        int minute = dateTimeBytes[5] & 0xFF;
        // 秒
        int second = dateTimeBytes[6] & 0xFF;
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);

    }
}

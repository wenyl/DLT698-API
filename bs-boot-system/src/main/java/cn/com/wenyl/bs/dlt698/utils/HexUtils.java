package cn.com.wenyl.bs.dlt698.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

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
     * 将两个字符的十六进制字符串转为一个 byte
     * @param hex 两个字符的十六进制字符串，如 "1A", "FF"
     * @return 对应的 byte 值
     */
    public static byte hexToByte(String hex) {
        if (hex == null || hex.length() != 2) {
            throw new IllegalArgumentException("必须是两个字符的十六进制字符串");
        }
        return (byte) Integer.parseInt(hex, 16);
    }
    /**
     * 字节转十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    public static String byteToHex(Byte b) {
        if(b == null){
            return null;
        }
        return String.format("%02X", b);
    }

}

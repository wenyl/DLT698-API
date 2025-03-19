package cn.com.wenyl.bs.dlt698.utils;

/**
 * 十六进制工具类
 */
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
}

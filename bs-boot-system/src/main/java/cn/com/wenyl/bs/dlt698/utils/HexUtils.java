package cn.com.wenyl.bs.dlt698.utils;

public class HexUtils {
    // 工具方法：字节数组转十六进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X ", b));
        }
        return hex.toString().trim();
    }
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
    public static String byteToHex(byte b) {
        return String.format("%02X", b);
    }
}

package cn.com.wenyl.bs.dlt698.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCDUtilsTest {
    byte[] addr = {(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA};

    @Test
    void convertToBCD() {
        // 测试10进制字符串到压缩BCD码字节数组的转换
        String decimalStr = "101010101010101010101010";
        byte[] bcdArray = BCDUtils.encodeBCD(decimalStr);
        System.out.println("10进制字符串: " + decimalStr);
        System.out.print("压缩BCD码字节数组: ");
        for (byte b : bcdArray) {
            System.out.print(String.format("%02X ", b));
        }
        System.out.println();
        byte[] bytes = BCDUtils.recoverOriginalBCD(decimalStr);
        System.out.println(HexUtils.bytesToHex(bytes));


    }

    @Test
    void decodeBCD() {
        String s = BCDUtils.decodeBCD(addr);
        System.out.println(s);
        byte[] bcdArray = BCDUtils.encodeBCD(s);
        for (byte b : bcdArray) {
            System.out.print(String.format("%02X ", b));
        }
        byte[] bytes = BCDUtils.recoverOriginalBCD(s);
        System.out.println(HexUtils.bytesToHex(bytes));
        System.out.println(s);
    }
}
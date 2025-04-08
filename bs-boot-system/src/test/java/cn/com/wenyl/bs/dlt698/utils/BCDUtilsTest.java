package cn.com.wenyl.bs.dlt698.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCDUtilsTest {
    private final static String serverAddress = "133232217120";
    @Test
    void encodeBCD() {
        System.out.println(HexUtils.bytesToHex(BCDUtils.encodeBCD(serverAddress)));
    }

    @Test
    void decodeBCD() {
    }
}
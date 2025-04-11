package cn.com.wenyl.bs.dlt698.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrameParseUtilsTest {

    @Test
    void getFrameDto() {
        byte[] bytes = HexUtils.hexStringToBytes("68 1E 00 81 05 11 11 11 11 11 11 00 9E 76 01 00 00 00 B4 07 E9 04 0A 04 0F 28 0F 00 00 6D 59 16");
        FrameParseUtils.getFrameDto(bytes);
    }
}
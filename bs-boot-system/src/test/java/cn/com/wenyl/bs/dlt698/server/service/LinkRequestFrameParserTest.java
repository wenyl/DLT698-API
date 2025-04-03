package cn.com.wenyl.bs.dlt698.server.service;

import cn.com.wenyl.bs.BSBootApplication;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BSBootApplication.class)
public class LinkRequestFrameParserTest {
    @Resource
    private LinkRequestFrameParser linkRequestFrameParser;
    @Test
    public void test(){
        linkRequestFrameParser.parseFrame(HexUtils.hexStringToBytes("68 1E 00 81 05 11 11 11 11 11 11 00 9E 76 01 00 00 00 B4 07 E9 04 03 04 0F 14 05 00 00 E8 B0 16"));
    }
}
package cn.com.wenyl.bs.dlt698.server.service;

import cn.com.wenyl.bs.BSBootApplication;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestData;
import cn.com.wenyl.bs.dlt698.server.entity.LinkRequestFrame;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BSBootApplication.class)
public class LinkRequestFrameParserTest {
    @Resource
    private LinkRequestFrameParser linkRequestFrameParser;
    @Test
    public void test(){
        LinkRequestFrame linkRequestFrame = linkRequestFrameParser.parseFrame(HexUtils.hexStringToBytes("68 1E 00 81 05 11 11 11 11 11 11 00 9E 76 01 00 00 00 B4 07 E9 04 03 04 0F 14 05 00 00 E8 B0 16"));
        LinkRequestData linkRequestData = linkRequestFrame.getLinkRequestData();
        log.info(String.valueOf(linkRequestData.getHeartbeatInterval()));
        log.info(linkRequestData.getRequestTime());
    }
}
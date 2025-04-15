package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.common.service.FrameParseService;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FrameParseServiceTest {
    @Resource
    private FrameParseService frameParseService;
    @Resource
    private DeviceMsgHisService deviceMsgHisService;
    @Test
    public void testParseBytes() throws Exception {
        int deviceId = 30;
        String deviceIp = "180.130.2.158";

        String sendHex = "68 3A 00 43 45 AA AA AA AA AA AA 00 BF 1A 09 07 05 F2 01 02 01 06 02 08 01 00 00 0A 00 64 19 68 17 00 43 05 01 00 20 03 25 01 00 A5 80 05 01 00 20 01 02 01 00 86 BB 16 00 EB B1 16";
        byte[] sendBytes = HexUtils.hexStringToBytes(sendHex);
        FrameDto sendFrameDto = FrameParseUtils.getFrameDto(sendBytes);
        deviceMsgHisService.save(sendFrameDto,deviceId,sendBytes);

        String hex = "68 3E 00 C3 05 11 11 11 11 11 11 00 8D E9 89 07 05 F2 01 02 01 01 24 FE FE FE FE 68 1E 00 C3 05 01 00 20 03 25 01 00 49 AD 85 01 00 20 01 02 01 01 05 00 00 00 00 00 00 9E F3 16 00 00 E3 2E 16";
        byte[] bytes = HexUtils.hexStringToBytes(hex);
        FrameDto frameDto = FrameParseUtils.getFrameDto(bytes);
        Integer msgId = deviceMsgHisService.save(frameDto,deviceId,bytes);
        frameParseService.frameParse(msgId,frameDto,deviceIp,bytes);
    }
}

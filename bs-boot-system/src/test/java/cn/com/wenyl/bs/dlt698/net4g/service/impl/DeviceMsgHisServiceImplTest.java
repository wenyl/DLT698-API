package cn.com.wenyl.bs.dlt698.net4g.service.impl;


import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.service.DeviceMsgHisService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeviceMsgHisServiceImplTest {
    @Resource
    private DeviceMsgHisService deviceMsgHisService;
    @Resource
    private DeviceChannelManager deviceChannelManager;
    @Test
    public void save() {
        int deviceId = 1;
        String hex1 = "68 1E 00 81 05 11 11 11 11 11 11 00 9E 76 01 00 00 00 B4 07 E9 04 0B 05 09 0C 28 00 00 8A 8B 16";
        byte[] byte1 = HexUtils.hexStringToBytes(hex1);
        FrameDto  frameDto1 = FrameParseUtils.getFrameDto(byte1);
        deviceMsgHisService.save(frameDto1,deviceId,byte1);

        String hex2 = "68 30 00 01 05 11 11 11 11 11 11 A0 14 BC 81 00 00 07 E9 04 0B 05 09 0C 28 00 00 07 E9 04 0B 05 09 0C 27 01 B8 07 E9 04 0B 05 09 0C 27 01 BC B1 98 16";
        byte[] byte2 = HexUtils.hexStringToBytes(hex2);
        FrameDto frameDto2 = FrameParseUtils.getFrameDto(byte2);
        deviceMsgHisService.save(frameDto2,deviceId,byte2);

        String hex3 = "68 58 00 83 05 11 11 11 11 11 11 A0 1C 70 88 02 09 01 31 06 02 00 06 00 20 22 02 00 00 20 1E 02 00 00 20 20 02 00 00 20 24 02 00 00 33 00 02 00 00 33 09 02 06 01 01 06 00 00 00 18 1C 07 E9 04 0A 0B 1D 26 00 16 00 01 01 02 02 51 45 00 02 00 11 01 04 08 80 00 00 1B C2 16";
        byte[] byte3 = HexUtils.hexStringToBytes(hex3);
        FrameDto frameDto3 = FrameParseUtils.getFrameDto(byte3);
        deviceMsgHisService.save(frameDto3,deviceId,byte3);

        String hex4 = "68 1E 00 81 05 11 11 11 11 11 11 00 9E 76 01 00 00 00 B4 07 E9 04 0B 05 09 0D 01 00 00 14 08 16";
        byte[] byte4 = HexUtils.hexStringToBytes(hex4);
        FrameDto frameDto4 = FrameParseUtils.getFrameDto(byte4);
        deviceMsgHisService.save(frameDto4,deviceId,byte4);

        String hex5 = "68 30 00 01 05 11 11 11 11 11 11 A0 14 BC 81 00 00 07 E9 04 0B 05 09 0D 01 00 00 07 E9 04 0B 05 09 0C 3B 03 6A 07 E9 04 0B 05 09 0C 3B 03 6B 6B C3 16";
        byte[] byte5 = HexUtils.hexStringToBytes(hex5);
        FrameDto frameDto5 = FrameParseUtils.getFrameDto(byte5);
        deviceMsgHisService.save(frameDto5,deviceId,byte5);
    }
}
package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.service.VoltageService;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VoltageServiceImplTest {
    @Resource
    private VoltageService voltageService;
    @Test
    public void getVoltage() {
        String hex = "68 24 00 C3 05 01 00 20 03 25 01 00 0E 29 85 01 00 20 00 02 00 01 01 03 12 08 C1 12 00 00 12 00 00 00 00 F2 EC 16";
        FrameDto frameDto = FrameParseUtils.getFrameDto(HexUtils.hexStringToBytes(hex));
        try {
            voltageService.getVoltage(33,459,frameDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
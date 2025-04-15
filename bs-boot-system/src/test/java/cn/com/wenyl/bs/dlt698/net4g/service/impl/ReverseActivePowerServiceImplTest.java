package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.service.ReverseActivePowerService;
import cn.com.wenyl.bs.dlt698.utils.FrameParseUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReverseActivePowerServiceImplTest {
    @Resource
    private ReverseActivePowerService reverseActivePowerService;
    @Test
    public void getReverseActivePower() {
        String hex = "68 34 00 C3 05 01 00 20 03 25 01 00 4B 58 85 01 00 00 20 02 00 01 01 05 06 00 00 00 00 06 00 00 00 00 06 00 00 00 00 06 00 00 00 00 06 00 00 00 00 00 00 34 32 16";
        FrameDto frameDto = FrameParseUtils.getFrameDto(HexUtils.hexStringToBytes(hex));
        try {
            reverseActivePowerService.getReverseActivePower(33,479,frameDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
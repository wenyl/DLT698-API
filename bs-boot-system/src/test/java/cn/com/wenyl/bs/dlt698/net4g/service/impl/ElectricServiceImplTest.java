package cn.com.wenyl.bs.dlt698.net4g.service.impl;

import cn.com.wenyl.bs.dlt698.net4g.service.ElectricService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElectricServiceImplTest {
    @Resource
    private ElectricService electricService;
    @Test
    public void getElectricCurrent() throws Exception {
        electricService.getElectricCurrent("119.62.124.187");
    }
}
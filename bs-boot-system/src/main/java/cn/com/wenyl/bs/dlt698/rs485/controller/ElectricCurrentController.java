package cn.com.wenyl.bs.dlt698.rs485.controller;

import com.alibaba.fastjson2.JSONException;
import cn.com.wenyl.bs.dlt698.rs485.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.rs485.service.ElectricCurrentService;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Api(tags="电流")
@RestController
@RequestMapping("/electricCurrent")
public class ElectricCurrentController {
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @Resource
    private ElectricCurrentService electricCurrentService;
    @ApiOperation(value="电流-读取电流", notes="电流-读取电流")
    @GetMapping("/getElectricCurrent")
    public R<Object> getElectricCurrent(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // 先链接电表
        carbonDeviceService.connectCarbonDevice(carbonDeviceAddress);
        return R.ok(electricCurrentService.getElectricCurrent(carbonDeviceAddress));
    }
}

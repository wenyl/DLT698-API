package cn.com.wenyl.bs.dlt698.client.controller;

import com.alibaba.fastjson2.JSONException;
import cn.com.wenyl.bs.dlt698.client.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.client.service.RAEEnergyService;
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

/**
 * RAEEnergy(反向有功电能量  Reverse active electrical energy)
 */
@Api(tags="反向有功电能量")
@RestController
@RequestMapping("/pAEEnergy")
public class RAEEnergyController {
    @Resource
    private RAEEnergyService raeEnergyService;
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @GetMapping("/getRAEEnergy")
    @ApiOperation(value="反向有功电能量-获取反向有功电能量", notes="反向有功电能量-获取反向有功电能量")
    public R<Object> getPAEEnergy(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress) throws JSONException, ExecutionException, InterruptedException, TimeoutException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        carbonDeviceService.connectCarbonDevice(carbonDeviceAddress);
        return R.ok(raeEnergyService.getRAEEnergy(carbonDeviceAddress));
    }
}

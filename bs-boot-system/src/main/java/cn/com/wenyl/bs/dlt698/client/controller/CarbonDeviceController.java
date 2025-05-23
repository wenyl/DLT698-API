package cn.com.wenyl.bs.dlt698.client.controller;

import cn.com.wenyl.bs.dlt698.client.service.CarbonDeviceService;
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

@Api(tags="碳表管理")
@RestController
@RequestMapping("/carbonDevice")
public class CarbonDeviceController {

    @Resource
    private CarbonDeviceService carbonDeviceService;
    @GetMapping("/getCarbonDeviceAddress")
    @ApiOperation(value="碳表管理-获取碳表地址", notes="碳表管理-获取碳表地址")

    public R<Object> getCarbonDeviceAddress() throws RuntimeException, TimeoutException, ExecutionException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return R.ok(carbonDeviceService.getCarbonDeviceAddress());
    }

    @GetMapping("/connectCarbonDevice")
    @ApiOperation(value="碳表管理-链接碳表", notes="碳表管理-链接碳表")
    public R<Object> connectCarbonDevice(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress) throws RuntimeException, TimeoutException, ExecutionException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return R.ok(carbonDeviceService.connectCarbonDevice(carbonDeviceAddress));
    }

}

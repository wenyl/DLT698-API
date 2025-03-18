package cn.com.wenyl.bs.dlt698.controller;

import cn.com.wenyl.bs.dlt698.service.ICarbonDeviceService;
import cn.com.wenyl.bs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Api(tags="碳表管理")
@RestController
@RequestMapping("/carbonDevice")
public class CarbonDeviceController {
    @Resource
    private ICarbonDeviceService carbonDeviceService;
    @GetMapping("/getCarbonDeviceAddress")
    @ApiOperation(value="碳表管理-获取碳表地址", notes="碳表管理-获取碳表地址")
    public R<JSONObject> getCarbonDeviceAddress()  throws RuntimeException,TimeoutException, ExecutionException, InterruptedException {
        return R.ok(carbonDeviceService.getCarbonDeviceAddress());
    }
}

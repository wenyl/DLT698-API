package cn.com.wenyl.bs.dlt698.controller;

import cn.com.wenyl.bs.dlt698.service.ForwardCarbonEmissionService;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Api(tags="正向碳排放管理")
@RestController
@RequestMapping("/forwardCarbonEmission")
public class ForwardCarbonEmissionController {
    @Resource
    private ForwardCarbonEmissionService carbonEmissionService;
    @GetMapping("/yesterdayTotal")
    @ApiOperation(value="正向碳排放管理-昨日累计", notes="正向碳排放管理-昨日累计")
    public R<Object> yesterdayCarbonAccumulate(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress) throws RuntimeException, TimeoutException, ExecutionException, InterruptedException {
        return R.ok(carbonEmissionService.yesterdayCarbonAccumulate(carbonDeviceAddress));
    }
}

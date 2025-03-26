package cn.com.wenyl.bs.dlt698.controller;

import cn.com.wenyl.bs.dlt698.service.VoltageService;
import cn.com.wenyl.bs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Api(tags="电压")
@RestController
@RequestMapping("/voltage")
public class VoltageController {
    @Resource
    private VoltageService voltageService;
    @ApiOperation(value="电压-读取电压", notes="电压-读取电压")
    @GetMapping("/getVoltage")
    public R<Object> getVoltage(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress) throws JSONException, ExecutionException, InterruptedException, TimeoutException {
        return R.ok(voltageService.getVoltage(carbonDeviceAddress));
    }
}

package cn.com.wenyl.bs.dlt698.controller;

import cn.com.wenyl.bs.dlt698.service.ElectricCurrentService;
import cn.com.wenyl.bs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Api(tags="电流")
@RestController
@RequestMapping("/electricCurrent")
public class ElectricCurrentController {
    @Resource
    private ElectricCurrentService electricCurrentService;
    @ApiOperation(value="电流-读取电流", notes="电流-读取电流")
    @GetMapping("/getElectricCurrent")
    public R<Object> getElectricCurrent(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException {
        return R.ok(electricCurrentService.getElectricCurrent(carbonDeviceAddress));
    }
}

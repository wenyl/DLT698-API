package cn.com.wenyl.bs.dlt698.controller;

import cn.com.wenyl.bs.dlt698.service.PAEEnergyService;
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

/**
 * PAEEnergy(正向有功电能量  Positive active electrical energy)
 */
@Api(tags="正向有功电能量")
@RestController
@RequestMapping("/pAEEnergy")
public class PAEEnergyController {
    @Resource
    private PAEEnergyService paeEnergyService;
    @GetMapping("/getPAEEnergy")
    @ApiOperation(value="正向有功电能量-获取正向有功电能量", notes="正向有功电能量-获取正向有功电能量")
    public R<Object> getPAEEnergy(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress) throws JSONException, ExecutionException, InterruptedException, TimeoutException {
        return R.ok(paeEnergyService.getPAEEnergy(carbonDeviceAddress));
    }
}

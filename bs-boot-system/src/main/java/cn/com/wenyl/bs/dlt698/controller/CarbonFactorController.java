package cn.com.wenyl.bs.dlt698.controller;

import cn.com.wenyl.bs.dlt698.service.CarbonFactorService;
import cn.com.wenyl.bs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apiguardian.api.API;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Api(tags="碳因子")
@RestController
@RequestMapping("/carbonFactor")
public class CarbonFactorController {

    @Resource
    private CarbonFactorService carbonFactorService;
    @ApiOperation(value="碳因子-设置1个碳因子(昨日单个因子)", notes="碳因子-设置1个碳因子(昨日单个因子)")
    @PutMapping("/set1CarbonFactor")
    public R<Object> set1CarbonFactor(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress,
                                      @RequestParam("carbonFactor") @ApiParam("碳因子") Double carbonFactor) throws ExecutionException, InterruptedException, TimeoutException {
        return R.ok(carbonFactorService.set1CarbonFactor(carbonDeviceAddress,carbonFactor));
    }
 }

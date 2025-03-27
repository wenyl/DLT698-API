package cn.com.wenyl.bs.dlt698.controller;

import cn.com.wenyl.bs.dlt698.entity.dto.CarbonFactorDto;
import cn.com.wenyl.bs.dlt698.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.service.CarbonFactorService;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Api(tags="碳因子")
@RestController
@RequestMapping("/carbonFactor")
public class CarbonFactorController {
    @Resource
    private CarbonDeviceService carbonDeviceService;

    @Resource
    private CarbonFactorService carbonFactorService;
    @ApiOperation(value="碳因子-设置1个碳因子(昨日单个因子)", notes="碳因子-设置1个碳因子(昨日单个因子)")
    @PutMapping("/set1CarbonFactor")
    public R<Object> set1CarbonFactor(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress,
                                      @RequestParam("carbonFactor") @ApiParam("碳因子") Double carbonFactor) throws ExecutionException, InterruptedException, TimeoutException {
        // 先链接电表
        carbonDeviceService.connectCarbonDevice(carbonDeviceAddress);
        return R.ok(carbonFactorService.set1CarbonFactor(carbonDeviceAddress,carbonFactor));
    }
    @ApiOperation(value="碳因子-设置多个碳因子(昨日24/96个因子)", notes="碳因子-设置多个碳因子(昨日24/96个因子)")
    @PutMapping("/setCarbonFactors")
    public R<Object> setCarbonFactors(@RequestBody @ApiParam("24/96个碳因子") CarbonFactorDto carbonFactorDto) throws ExecutionException, InterruptedException, TimeoutException {
        // 先链接电表
        carbonDeviceService.connectCarbonDevice(carbonFactorDto.getCarbonDeviceAddress());
        if(carbonFactorDto.getCarbonFactor() == null || carbonFactorDto.getCarbonFactor().length == 24 || carbonFactorDto.getCarbonFactor().length == 96){
            return R.ok(carbonFactorService.setCarbonFactors(carbonFactorDto));

        }
        return R.error("需要24或者96个碳因子");
    }

 }

package cn.com.wenyl.bs.dlt698.net4g.controller;


import cn.com.wenyl.bs.dlt698.common.entity.dto.CarbonFactorDto;
import cn.com.wenyl.bs.dlt698.net4g.service.CarbonFactorService;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 电碳因子 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Api(tags="碳因子")
@RestController
@RequestMapping("/carbonFactor")
public class CarbonFactorController {
    @Resource
    private CarbonFactorService carbonFactorService;
    @ApiOperation(value="碳因子-设置1个碳因子(昨日单个因子)", notes="碳因子-设置1个碳因子(昨日单个因子)")
    @PutMapping("/setCarbonFactor")
    public R<Object> set1CarbonFactor(@RequestParam("deviceIp") @ApiParam("碳表地址") String deviceIp,
                                      @RequestParam("carbonFactor") @ApiParam("碳因子") Double carbonFactor) throws Exception {
        carbonFactorService.setCarbonFactor(deviceIp,carbonFactor);
        return R.ok("操作成功");
    }
    @ApiOperation(value="碳因子-设置多个碳因子(昨日24/96个因子)", notes="碳因子-设置多个碳因子(昨日24/96个因子)")
    @PutMapping("/setCarbonFactors")
    public R<Object> setCarbonFactors(@RequestBody @ApiParam("24/96个碳因子") CarbonFactorDto carbonFactorDto) throws Exception{
        // 先链接电表
        if(carbonFactorDto.getCarbonFactor() == null || carbonFactorDto.getCarbonFactor().length == 24 || carbonFactorDto.getCarbonFactor().length == 96){
            carbonFactorService.setCarbonFactors(carbonFactorDto);
            return R.ok("操作成功");
        }
        return R.error("需要24或者96个碳因子");
    }

}


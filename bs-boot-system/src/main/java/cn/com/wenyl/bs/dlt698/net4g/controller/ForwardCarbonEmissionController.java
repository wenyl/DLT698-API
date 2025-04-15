package cn.com.wenyl.bs.dlt698.net4g.controller;


import cn.com.wenyl.bs.dlt698.net4g.service.ForwardCarbonEmissionService;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 正向碳排放 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Api(tags="正向碳排放")
@RestController
@RequestMapping("/forwardCarbonEmission")
public class ForwardCarbonEmissionController {

    @Resource
    private ForwardCarbonEmissionService forwardCarbonEmissionService;

    @GetMapping("/getForwardCarbonEmission")
    @ApiOperation(value="正向碳排放-上一日正向碳排放累计量", notes="正向碳排放-上一日正向碳排放累计量")
    public R<Object> getForwardCarbonEmission(@RequestParam("deviceIp") @ApiParam("碳表地址") String deviceIp) throws Exception {
        forwardCarbonEmissionService.getForwardCarbonEmission(deviceIp);
        return R.ok("操作成功");
    }
}


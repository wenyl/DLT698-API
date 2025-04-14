package cn.com.wenyl.bs.dlt698.net4g.controller;


import cn.com.wenyl.bs.dlt698.net4g.service.VoltageService;
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
 * 设备电压历史值 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Api(tags="电压")
@RestController
@RequestMapping("/voltage")
public class VoltageController {
    @Resource
    private VoltageService voltageService;

    @ApiOperation(value="电压-读取电压", notes="电压-读取电压")
    @GetMapping("/getVoltage")
    public R<Object> getVoltage(@RequestParam("deviceIp") @ApiParam("碳表地址") String deviceIp) throws Exception{
        voltageService.getVoltage(deviceIp);
        return R.ok("查询成功");
    }
}


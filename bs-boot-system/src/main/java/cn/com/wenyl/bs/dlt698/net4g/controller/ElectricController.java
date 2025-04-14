package cn.com.wenyl.bs.dlt698.net4g.controller;


import cn.com.wenyl.bs.dlt698.net4g.service.ElectricService;
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
 * 设备历史电流值 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Api(tags="电流")
@RestController
@RequestMapping("/electric")
public class ElectricController {
    @Resource
    private ElectricService electricService;
    @ApiOperation(value="电流-读取电流", notes="电流-读取电流")
    @GetMapping("/getElectricCurrent")
    public R<Object> getElectricCurrent(@RequestParam("deviceIp") @ApiParam("碳表地址") String deviceIp) throws Exception{
        // 先链接电表
        electricService.getElectricCurrent(deviceIp);
        return R.ok("查询成功");
    }
}


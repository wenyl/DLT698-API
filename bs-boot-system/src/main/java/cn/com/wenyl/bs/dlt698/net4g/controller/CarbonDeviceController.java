package cn.com.wenyl.bs.dlt698.net4g.controller;


import cn.com.wenyl.bs.dlt698.net4g.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 碳表信息 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/carbonDevice")
public class CarbonDeviceController {
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @GetMapping("/getAddress")
    @ApiOperation(value="碳表管理-获取碳表地址", notes="碳表管理-获取碳表地址")
    public R<Object> getAddress(@RequestParam("deviceIp") String deviceIp) throws Exception{
        carbonDeviceService.getAddress(deviceIp);
        return R.ok();
    }
}


package cn.com.wenyl.bs.dlt698.net4g.controller;


import cn.com.wenyl.bs.dlt698.net4g.service.ForwardActivePowerService;
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
 * 正向有功 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Api(tags="正向有功电能量")
@RestController
@RequestMapping("/forwardActivePower")
public class ForwardActivePowerController {
    @Resource
    private ForwardActivePowerService forwardActivePowerService;

    @GetMapping("/getForwardActivePower")
    @ApiOperation(value="正向有功电能量-获取正向有功电能量", notes="正向有功电能量-获取正向有功电能量")
    public R<Object> getForwardActivePower(@RequestParam("deviceIp") @ApiParam("碳表地址") String deviceIp) throws Exception {
        forwardActivePowerService.getForwardActivePower(deviceIp);
        return R.ok("操作成功");
    }
}


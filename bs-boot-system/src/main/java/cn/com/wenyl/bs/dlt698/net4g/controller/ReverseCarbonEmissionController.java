package cn.com.wenyl.bs.dlt698.net4g.controller;


import cn.com.wenyl.bs.dlt698.common.constants.*;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.common.service.impl.FrameBuildProcessor;
import cn.com.wenyl.bs.dlt698.common.service.impl.GetRequestNormalFrameBuilder;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.service.ReverseCarbonEmissionService;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 反向碳排放 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Api(tags="反向碳排放")
@RestController
@RequestMapping("/reverseCarbonEmission")
public class ReverseCarbonEmissionController {
    @Resource
    private ReverseCarbonEmissionService reverseCarbonEmissionService;

    @GetMapping("/getReverseCarbonEmission")
    @ApiOperation(value="反向碳排放-上一日反向碳排放累计量", notes="反向碳排放-上一日反向碳排放累计量")
    public R<Object> getReverseCarbonEmission(String deviceIp) throws Exception {
        reverseCarbonEmissionService.getReverseCarbonEmission(deviceIp);
        return R.ok("操作成功");

    }
}


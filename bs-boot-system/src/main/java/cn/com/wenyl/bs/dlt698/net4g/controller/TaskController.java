package cn.com.wenyl.bs.dlt698.net4g.controller;

import cn.com.wenyl.bs.dlt698.net4g.service.impl.CollectDataTask;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api("定时任务")
@RequestMapping("/task")
@RestController
public class TaskController {

    @Resource
    private CollectDataTask collectDataTask;

    @GetMapping("/collectDataTask")
    @ApiOperation(value="定时任务-采集设备数据", notes="定时任务-采集设备数据")
    public R<String> collectDataTask() throws Exception{
        collectDataTask.collectDataTask();
        return R.ok("操作成功");
    }
}

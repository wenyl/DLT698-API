package cn.com.wenyl.bs.dlt698.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.com.wenyl.bs.dlt698.entity.dto.CarbonDeviceTaskMsgDto;
import cn.com.wenyl.bs.dlt698.service.CarbonDeviceService;
import cn.com.wenyl.bs.dlt698.service.CarbonDeviceTaskService;
import cn.com.wenyl.bs.dlt698.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 碳表任务 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2025-03-27
 */
@Api(tags="碳表任务")
@RestController
@RequestMapping("/carbonDeviceTask")
public class CarbonDeviceTaskController {

    @Resource
    private CarbonDeviceTaskService carbonDeviceTaskService;
    @Resource
    private CarbonDeviceService carbonDeviceService;
    @GetMapping("/pageScreenData")
    @ApiOperation(value = "碳表任务-大屏数据分页",notes = "碳表任务-屏数据分页")
    public R<IPage<CarbonDeviceTaskMsgDto>> pageScreenData(@RequestParam("currentPage")@ApiParam("当前页码") int currentPage,
                                                           @RequestParam("pageSize")@ApiParam("数据条数")int pageSize,
                                                           @RequestParam("carbonDeviceAddress")@ApiParam("碳表地址")String carbonDeviceAddress){
        return R.ok(carbonDeviceTaskService.pageScreenData(currentPage,pageSize,carbonDeviceAddress));
    }

    @GetMapping("/getData")
    @ApiOperation(value="碳表管理-获取数据(电流、电压、正向有功电能量、反向有功电能量、正向碳排放管理-昨日累计、反向碳排放管理-昨日累计)", notes="碳表管理-获取数据(电流、电压、正向有功电能量、反向有功电能量、正向碳排放管理-昨日累计、反向碳排放管理-昨日累计)")
    public R<Object> getData(@RequestParam("carbonDeviceAddress") @ApiParam("碳表地址") String carbonDeviceAddress) throws RuntimeException, TimeoutException, ExecutionException, InterruptedException {
        carbonDeviceService.connectCarbonDevice(carbonDeviceAddress);
        return R.ok(carbonDeviceTaskService.getData(carbonDeviceAddress));
    }
}


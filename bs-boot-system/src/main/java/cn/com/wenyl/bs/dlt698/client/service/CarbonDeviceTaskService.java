package cn.com.wenyl.bs.dlt698.client.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.com.wenyl.bs.dlt698.common.entity.CarbonDeviceTask;
import cn.com.wenyl.bs.dlt698.common.entity.dto.CarbonDeviceTaskMsgDto;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 碳表任务 服务类
 * </p>
 *
 * @author baomidou
 * @since 2025-03-26
 */
public interface CarbonDeviceTaskService extends IService<CarbonDeviceTask> {

    /**
     * 查询指定碳表历史数据
     * @param currentPage 当前页
     * @param pageSize 每页数据条数
     * @param carbonDeviceAddress 碳表地址
     * @return 历史数据
     */
    IPage<CarbonDeviceTaskMsgDto> pageScreenData(int currentPage, int pageSize,String carbonDeviceAddress);
    /**
     * 获取数据(电流、电压、正向有功电能量、反向有功电能量、正向碳排放管理-昨日累计、反向碳排放管理-昨日累计)
     * @param carbonDeviceAddress 碳表地址
     * @return 返回数据
     */
    Object getData(String carbonDeviceAddress) throws RuntimeException, TimeoutException, ExecutionException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}

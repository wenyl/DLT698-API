package cn.com.wenyl.bs.dlt698.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.com.wenyl.bs.dlt698.client.entity.CarbonDeviceTask;
import cn.com.wenyl.bs.dlt698.client.entity.dto.CarbonDeviceTaskDto;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 碳表任务 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2025-03-26
 */
public interface CarbonDeviceTaskMapper extends BaseMapper<CarbonDeviceTask> {

    Page<CarbonDeviceTaskDto> pageScreenData(Page<CarbonDeviceTaskDto> page,@Param("carbonDeviceAddress") String carbonDeviceAddress);
}


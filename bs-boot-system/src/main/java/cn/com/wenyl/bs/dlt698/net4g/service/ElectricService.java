package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.Electric;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 设备历史电流值 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface ElectricService extends IService<Electric> {
    /**
     * 获取三相电表电流
     * @param deviceIp 碳表地址
     */
    void getElectricCurrent(String deviceIp)  throws Exception;

    /**
     * 获取帧数据中的电流
     * @param deviceId 设备ID
     * @param msgId 消息ID
     * @param frameDto 帧数据传输对象
     */
    void getElectric(Integer deviceId, Integer msgId, FrameDto frameDto)  throws Exception;
}

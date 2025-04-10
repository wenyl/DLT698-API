package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.net4g.entity.CarbonDevice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 碳表信息 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface CarbonDeviceService extends IService<CarbonDevice> {

    /**
     * 设备状态为在线
     * @param deviceId 设备ID
     */
    void isAlive(Integer deviceId);

    /**
     * 设备状态为离线
     * @param deviceId 设备ID
     */
    void isDead(Integer deviceId);
}

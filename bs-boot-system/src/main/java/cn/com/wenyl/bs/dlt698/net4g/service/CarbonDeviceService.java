package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
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
    /**
     * 获取设备地址
     *
     * @param deviceIp 设备IP
     */
    void getAddress(String deviceIp) throws Exception;
    /**
     * 获取设备地址
     *
     * @param deviceIp 设备IP
     * @param frameDto 收到的帧数据
     */
    void getAddress(String deviceIp, FrameDto frameDto) throws Exception;

    /**
     * 获取缓存和数据库中的碳表地址，缓存中有就直接拿，没有就数据库查询
     *
     * @param deviceIp 设备IP
     */
    byte[] getDeviceAddress(String deviceIp);
}

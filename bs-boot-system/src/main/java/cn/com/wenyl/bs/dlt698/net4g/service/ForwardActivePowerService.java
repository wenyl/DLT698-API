package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.ForwardActivePower;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 正向有功 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface ForwardActivePowerService extends IService<ForwardActivePower> {

    /**
     * 获取帧数据中的正向有功值
     * @param deviceId 设备ID
     * @param msgId 消息ID
     * @param frameDto frame传输对象
     */
    void getForwardActivePower(Integer deviceId, Integer msgId, FrameDto frameDto) throws Exception;

    /**
     * 获取指定设备的正向有功值
     * @param deviceIp 设备地址
     * @throws Exception 异常
     */
    void getForwardActivePower(String deviceIp) throws Exception;
}

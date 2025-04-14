package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.ReverseActivePower;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 反向有功 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface ReverseActivePowerService extends IService<ReverseActivePower> {

    /**
     * 解析反向有功
     * @param deviceId 设备ID
     * @param msgId 消息ID
     * @param frameDto frame传输对象
     */
    void getReverseActivePower(Integer deviceId, Integer msgId, FrameDto frameDto) throws Exception;

    /**
     * 解析反向有功
     * @param deviceIp 设备IP
     */
    void getReverseActivePower(String deviceIp) throws Exception;
}

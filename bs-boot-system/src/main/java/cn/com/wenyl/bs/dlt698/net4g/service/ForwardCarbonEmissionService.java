package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.ForwardCarbonEmission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 正向碳排放 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface ForwardCarbonEmissionService extends IService<ForwardCarbonEmission> {

    /**
     * 获取设备的正向碳排放
     * @param deviceIp 设备IP
     */
    void getForwardCarbonEmission(String deviceIp) throws Exception;

    /**
     * 获取帧数据中的碳排放数据
     * @param deviceId 设备ID
     * @param msgId 消息ID
     * @param frameDto frame传输对象
     */
    void getForwardCarbonEmission(Integer deviceId, Integer msgId, FrameDto frameDto) throws Exception;
}

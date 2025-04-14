package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.ReverseCarbonEmission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 反向碳排放 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface ReverseCarbonEmissionService extends IService<ReverseCarbonEmission> {

    /**
     * 获取反向碳排放
     * @param deviceIp 设备IP
     */
    void getReverseCarbonEmission(String deviceIp) throws Exception;


    /**
     * 获取帧数据中的反向碳排放
     * @param deviceId 设备ID
     * @param msgId 消息ID
     * @param frameDto 帧传输对象
     */
    void getReverseCarbonEmission(Integer deviceId, Integer msgId, FrameDto frameDto);
}

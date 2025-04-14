package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.Voltage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 设备电压历史值 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface VoltageService extends IService<Voltage> {
    /**
     * 查询指定设备的三相电压
     * @param deviceIp 设备地址
     */
    void getVoltage(String deviceIp) throws Exception;

    /**
     * 获取帧数据中的电压值
     * @param deviceId 设备ID
     * @param msgId 消息ID
     * @param frameDto 帧数据传输对象
     */
    void getVoltage(Integer deviceId,Integer msgId,FrameDto frameDto) throws Exception;
}

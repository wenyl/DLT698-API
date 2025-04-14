package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceMsgHis;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 设备消息历史 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface DeviceMsgHisService extends IService<DeviceMsgHis> {

    /**
     * 将收到的消息存储到历史记录，并查询是否有对应的消息如果有就吧对应消息记录到消息关系表中
     * @param frameDto 帧数据传输对象
     * @param deviceId 设备ID
     * @param bytes 帧原始数据
     * @return  数据ID
     */
    Integer save(FrameDto frameDto, Integer deviceId, byte[] bytes);
}

package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.net4g.entity.DeviceMsgHis;
import cn.com.wenyl.bs.dlt698.net4g.entity.StationDeviceMsgRela;
import cn.com.wenyl.bs.dlt698.net4g.entity.dto.DeviceMsgHisRela;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 设备消息历史关系 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface StationDeviceMsgRelaService extends IService<StationDeviceMsgRela> {

    /**
     * 找到该消息对应的消息，并保存到关系表
     * @param msgHisRela 关联消息条件
     */
    void saveRelaMsg(DeviceMsgHisRela msgHisRela);
}

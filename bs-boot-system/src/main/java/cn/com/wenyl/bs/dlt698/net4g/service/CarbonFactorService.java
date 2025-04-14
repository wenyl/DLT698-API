package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.CarbonFactorDto;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;
import cn.com.wenyl.bs.dlt698.net4g.entity.CarbonFactor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 电碳因子 服务类
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
public interface CarbonFactorService extends IService<CarbonFactor> {

    /**
     * 设置电碳因子
     * @param deviceIp 设备IP
     * @param carbonFactor 电碳因子
     */
    void setCarbonFactor(String deviceIp, Double carbonFactor) throws Exception;

    /**
     * 设置多个电碳因子
     * @param carbonFactorDto 碳因子
     */
    void setCarbonFactors(CarbonFactorDto carbonFactorDto) throws Exception;

    /**
     * 获取设置结果
     * @param deviceId 设备ID
     * @param msgId 消息ID
     * @param frameDto 数据传输对象
     */
    void getSetResult(Integer deviceId, Integer msgId, FrameDto frameDto);
}

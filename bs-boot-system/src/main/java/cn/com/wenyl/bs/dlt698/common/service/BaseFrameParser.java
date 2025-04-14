package cn.com.wenyl.bs.dlt698.common.service;

import cn.com.wenyl.bs.dlt698.common.entity.Frame;
import cn.com.wenyl.bs.dlt698.common.entity.LinkUserData;
import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;

/**
 * 基础帧解析服务
 * @param <T> 帧数据
 */
public interface BaseFrameParser<T extends Frame,G extends LinkUserData> {
    /**
     * 将帧字节数据解析为帧数据
     * @param frameDto 解析的frame传输对象
     * @return 帧数据
     * @throws RuntimeException 解析异常
     */
    T parseFrame(FrameDto frameDto) throws RuntimeException;
    /**
     * 解析帧的用户链路数据
     * @param userDataBytes 用户链路数据字节信息
     * @return 用户链路数据
     */
    G parseLinkUserData(byte[] userDataBytes);
    /**
     * 解析帧中的实际数据，这里帧实例中会包含其他数据
     * @param frame 帧信息
     * @return 实际数据
     */
    Object getData(T frame) throws RuntimeException;
}

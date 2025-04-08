package cn.com.wenyl.bs.dlt698.common.service;

import cn.com.wenyl.bs.dlt698.common.constants.RequestType;
import cn.com.wenyl.bs.dlt698.common.entity.Frame;

/**
 * 基础帧构建服务
 * @param <T> 泛型
 */
public interface BaseFrameBuilder<T extends Frame> {
    /**
     * 组装完整帧数据
     * @param frame frame信息
     * @return 完整帧数据
     */
    byte[] buildFrame(T frame);

    /**
     * 构建帧的链路用户数据
     * @param frame 帧
     * @return 帧的链路用户数据
     */
    byte[] buildLinkUserData(T frame);
}

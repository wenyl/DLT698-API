package cn.com.wenyl.bs.dlt698.service;


import cn.com.wenyl.bs.dlt698.entity.Frame;

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

    /**
     * 构建长度域
     * @param length 长度
     * @return 长度域
     */
    byte[] buildFrameLength(int length);

    /**
     * 构建HCS校验信息
     * @param frameHead 帧头
     * @return HCS校验信息
     */
    byte[] buildHCS(byte[] frameHead);

    /**
     * 构建FCS校验信息
     * @param frameBody 帧体
     * @return FCS校验信息
     */
    byte[] buildFCS(byte[] frameBody);
}

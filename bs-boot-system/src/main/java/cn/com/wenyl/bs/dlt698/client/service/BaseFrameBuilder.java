package cn.com.wenyl.bs.dlt698.client.service;


import cn.com.wenyl.bs.dlt698.client.constants.RequestType;
import cn.com.wenyl.bs.dlt698.common.Frame;



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
     * 根据帧头信息和链路用户数据构建帧的字节数据
     * @param frameHead 帧头信息
     * @param linkUserData 链路用户数据
     * @return 返回帧的字节信息
     */
    byte[] buildFrame(byte[] frameHead, byte[] linkUserData);

    /**
     * 构建一个客户端发起的GetRequest类型请求,这个在具体的子类中实现就好
     * @param functionCode 功能码
     * @param scramblingCodeFlag sc控制标识
     * @param frameFlag 分帧标识
     * @param requestType 请求类型
     * @param addressType 地址类型
     * @param logicAddress 逻辑地址
     * @param serverAddress 服务器地址
     * @param clientAddress 客户端地址
     * @return 返回帧信息
     */
    Frame getFrame(int functionCode, int scramblingCodeFlag,
                   int frameFlag, RequestType requestType, int addressType,
                   int logicAddress, byte[] serverAddress, byte clientAddress
    );

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

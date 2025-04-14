package cn.com.wenyl.bs.dlt698.net4g.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;

/**
 * 碳表和主站连接接口
 */
public interface LinkService {
    /**
     * 碳表登录主站
     * @param deviceIp 碳表IP
     * @param frameDto 帧数据传输对象
     * @throws Exception 异常
     */
    void login(String deviceIp, FrameDto frameDto) throws Exception ;

    /**
     * 碳表心跳信息
     * @param deviceIp 碳表IP
     * @param frameDto 帧数据传输对象
     * @throws Exception 异常
     */
    void heartbeat(String deviceIp,FrameDto frameDto) throws Exception ;

    /**
     * 碳表退出登录
     * @param deviceIp 碳表IP
     * @param frameDto 帧数据传输对象
     * @throws Exception 异常
     */
    void logout(String deviceIp,FrameDto frameDto) throws Exception ;
}

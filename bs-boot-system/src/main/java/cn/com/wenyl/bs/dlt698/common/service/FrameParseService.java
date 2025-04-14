package cn.com.wenyl.bs.dlt698.common.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;

import java.lang.reflect.InvocationTargetException;

/**
 * 帧数据解析服务
 */
public interface FrameParseService {

    /**
     * 解析设备发送的字节数据
     * @param msgId 消息ID
     * @param frameDto frame传输对象
     * @param deviceIp 设备IP
     * @param bytes 设备发送的字节数据
     */
    void frameParse(Integer msgId,FrameDto frameDto,String deviceIp,byte[] bytes) throws Exception;
}

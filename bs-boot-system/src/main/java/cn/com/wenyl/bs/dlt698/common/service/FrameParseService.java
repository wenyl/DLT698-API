package cn.com.wenyl.bs.dlt698.common.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;

import java.lang.reflect.InvocationTargetException;

public interface FrameParseService {
    /**
     * 处理linkRequest
     * @param frameDto frame传输对象
     * @param bytes 原始报文
     */
    void linkRequest(String deviceId,FrameDto frameDto, byte[] bytes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    /**
     * 解析设备发送的字节数据
     * @param deviceId 设备ID
     * @param bytes 设备发送的字节数据
     */
    void frameParse(String deviceId,byte[] bytes) throws RuntimeException,InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}

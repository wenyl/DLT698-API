package cn.com.wenyl.bs.dlt698.common.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;

import java.lang.reflect.InvocationTargetException;

/**
 * 帧数据解析服务
 */
public interface FrameParseService {
    /**
     * 处理login
     * @param frameDto frame传输对象
     * @param bytes 原始报文
     */
    void login(String deviceId,FrameDto frameDto, byte[] bytes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    /**
     * 处理heartBeat
     * @param frameDto frame传输对象
     * @param bytes 原始报文
     */
    void heartbeat(String deviceId,FrameDto frameDto, byte[] bytes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    /**
     * 处理heartBeat
     * @param frameDto frame传输对象
     * @param bytes 原始报文
     */
    void logout(String deviceId,FrameDto frameDto, byte[] bytes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    /**
     * 解析设备发送的字节数据
     * @param deviceId 设备ID
     * @param bytes 设备发送的字节数据
     */
    void frameParse(String deviceId,byte[] bytes) throws RuntimeException,InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}

package cn.com.wenyl.bs.dlt698.common.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.FrameDto;

import java.lang.reflect.InvocationTargetException;

/**
 * 帧数据解析服务
 */
public interface FrameParseService {
    /**
     * 处理login
     * @param deviceIp 设备IP
     * @param frameDto frame传输对象
     */
    void login(String deviceIp,FrameDto frameDto) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    /**
     * 处理heartBeat
     * @param deviceIp 设备IP
     * @param frameDto frame传输对象
     */
    void heartbeat(String deviceIp,FrameDto frameDto) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    /**
     * 处理heartBeat
     * @param deviceIp 设备IP
     * @param frameDto frame传输对象
     */
    void logout(String deviceIp,FrameDto frameDto) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    /**
     * 解析设备发送的字节数据
     * @param frameDto frame传输对象
     * @param deviceIp 设备IP
     * @param bytes 设备发送的字节数据
     */
    void frameParse(FrameDto frameDto,String deviceIp,byte[] bytes) throws RuntimeException,InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}

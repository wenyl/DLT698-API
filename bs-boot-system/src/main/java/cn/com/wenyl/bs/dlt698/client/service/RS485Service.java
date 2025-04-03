package cn.com.wenyl.bs.dlt698.client.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * RS485服务
 */
public interface RS485Service {
    /**
     * @param data 帧字节信息
     * @return 电表返回的数据
     * @throws RuntimeException
     * @throws TimeoutException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    byte[] sendByte(byte[] data)throws RuntimeException,TimeoutException, ExecutionException, InterruptedException ;
}

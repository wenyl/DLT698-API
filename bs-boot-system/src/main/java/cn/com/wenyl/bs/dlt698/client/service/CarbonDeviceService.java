package cn.com.wenyl.bs.dlt698.client.service;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface CarbonDeviceService {


    /**
     * 获取设备地址
     *
     * @return 设备地址
     */
    Object getCarbonDeviceAddress() throws RuntimeException,TimeoutException, ExecutionException, InterruptedException ;

    /**
     * 链接指定碳表
     * @param carbonDeviceAddress 碳表地址
     * @return 链接信息
     */
    Object connectCarbonDevice(String carbonDeviceAddress)throws RuntimeException,TimeoutException, ExecutionException, InterruptedException  ;


}
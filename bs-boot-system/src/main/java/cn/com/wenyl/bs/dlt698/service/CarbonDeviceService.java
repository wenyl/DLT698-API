package cn.com.wenyl.bs.dlt698.service;



import com.alibaba.fastjson2.JSONException;

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

    /**
     * 获取数据(电流、电压、正向有功电能量、反向有功电能量、正向碳排放管理-昨日累计、反向碳排放管理-昨日累计)
     * @param carbonDeviceAddress 碳表地址
     * @return 返回数据
     */
    Object getData(String carbonDeviceAddress)throws RuntimeException,TimeoutException, ExecutionException, InterruptedException  ;
}
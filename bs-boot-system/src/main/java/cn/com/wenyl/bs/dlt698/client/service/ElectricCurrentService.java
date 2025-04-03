package cn.com.wenyl.bs.dlt698.client.service;

import com.alibaba.fastjson2.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 电流服务
 */
public interface ElectricCurrentService {
    /**
     * 获取三相电表电流
     * @param carbonDeviceAddress 碳表地址
     * @return 返回ABC三相电流
     */
    Object getElectricCurrent(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException;
}

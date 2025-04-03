package cn.com.wenyl.bs.dlt698.client.service;

import com.alibaba.fastjson2.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 正向有功电能量服务
 */
public interface PAEEnergyService {
    Object getPAEEnergy(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException;
}

package cn.com.wenyl.bs.dlt698.service;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 反向有功电能量服务
 */
public interface RAEEnergyService {
    Object getRAEEnergy(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException;
}

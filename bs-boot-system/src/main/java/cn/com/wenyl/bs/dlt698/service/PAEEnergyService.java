package cn.com.wenyl.bs.dlt698.service;

import cn.com.wenyl.bs.utils.R;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 正向有功电能量服务
 */
public interface PAEEnergyService {
    Object getPAEEnergy(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException;
}

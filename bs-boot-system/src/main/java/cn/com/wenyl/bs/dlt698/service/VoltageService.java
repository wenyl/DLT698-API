package cn.com.wenyl.bs.dlt698.service;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 电压服务
 */
public interface VoltageService {
    /**
     * 查询指定设备的三相电压
     * @param carbonDeviceAddress 设备地址
     * @return 三相电压
     */
    Object getVoltage(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, JSONException;
}

package cn.com.wenyl.bs.dlt698.service;


import cn.com.wenyl.bs.dlt698.entity.FrameData;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ICarbonDeviceService {


    /**
     * 获取设备地址
     *
     * @return 设备地址
     */
    JSONObject getCarbonDeviceAddress() throws RuntimeException,TimeoutException, ExecutionException, InterruptedException ;
}
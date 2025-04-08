package cn.com.wenyl.bs.dlt698.client.service;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ForwardCarbonEmissionService {
    Object yesterdayCarbonAccumulate(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}

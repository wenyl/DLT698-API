package cn.com.wenyl.bs.dlt698.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ReverseCarbonEmissionService {
    Object yesterdayCarbonAccumulate(String carbonDeviceAddress) throws ExecutionException, InterruptedException, TimeoutException;
}

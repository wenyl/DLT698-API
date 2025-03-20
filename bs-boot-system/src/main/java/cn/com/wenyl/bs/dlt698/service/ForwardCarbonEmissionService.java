package cn.com.wenyl.bs.dlt698.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ForwardCarbonEmissionService {
    Object yesterdayCarbonAccumulate(String deviceAddress) throws ExecutionException, InterruptedException, TimeoutException;
}

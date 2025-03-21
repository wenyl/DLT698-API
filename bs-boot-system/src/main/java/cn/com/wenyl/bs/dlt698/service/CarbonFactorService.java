package cn.com.wenyl.bs.dlt698.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface CarbonFactorService {

    Object set1CarbonFactor(String carbonDeviceAddress,Double carbonFactor)  throws ExecutionException, InterruptedException, TimeoutException;
}

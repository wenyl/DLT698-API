package cn.com.wenyl.bs.dlt698.client.service;

import cn.com.wenyl.bs.dlt698.common.entity.dto.CarbonFactorDto;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface CarbonFactorService {

    Object set1CarbonFactor(String carbonDeviceAddress,Double carbonFactor) throws ExecutionException, InterruptedException, TimeoutException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    Object setCarbonFactors(CarbonFactorDto carbonFactorDto) throws ExecutionException, InterruptedException, TimeoutException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

}

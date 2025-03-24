package cn.com.wenyl.bs.dlt698.service;

import cn.com.wenyl.bs.dlt698.entity.dto.CarbonFactorDto;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface CarbonFactorService {

    Object set1CarbonFactor(String carbonDeviceAddress,Double carbonFactor)  throws ExecutionException, InterruptedException, TimeoutException;

    Object setCarbonFactors(CarbonFactorDto carbonFactorDto) throws ExecutionException, InterruptedException, TimeoutException;

}

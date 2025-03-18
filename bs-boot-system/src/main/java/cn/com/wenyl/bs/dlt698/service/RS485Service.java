package cn.com.wenyl.bs.dlt698.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface RS485Service {
    byte[] sendByte(byte[] data)throws RuntimeException,TimeoutException, ExecutionException, InterruptedException ;
}

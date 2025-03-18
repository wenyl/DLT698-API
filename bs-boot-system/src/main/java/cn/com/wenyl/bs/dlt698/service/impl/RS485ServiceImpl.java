package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.RS485;
import cn.com.wenyl.bs.dlt698.service.RS485Service;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service("rs485ServiceImpl")
@Slf4j
public class RS485ServiceImpl implements RS485Service {

    @Override
    public byte[] sendByte(byte[] data) throws RuntimeException, TimeoutException,ExecutionException, InterruptedException {
        CompletableFuture<ResponseEntity<byte[]>> future = new CompletableFuture<>();
        SerialCommUtils serial = SerialCommUtils.getInstance();
        try{
            // 设置数据接收的回调
            serial.setDataReceivedListener(receivedData -> {
                // 监听到数据后，完成 Future 返回结果
                future.complete(ResponseEntity.ok(receivedData));
            });
            // 打开串口并发送数据
            if(!serial.openPort()){
                throw new RuntimeException("串口打开失败: " + RS485.PART_NAME);
            }
            serial.sendData(data);
            return future.get(3, TimeUnit.MINUTES).getBody();
        }catch (TimeoutException e) {
            log.error("rs485通信等待超时");
            future.completeExceptionally(e);
            throw e; // 超时
        } catch (ExecutionException | InterruptedException e) {
            future.completeExceptionally(e);
            throw e; // 其他异常
        }
    }
}

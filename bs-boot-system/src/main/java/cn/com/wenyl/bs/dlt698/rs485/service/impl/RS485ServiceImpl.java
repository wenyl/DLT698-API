package cn.com.wenyl.bs.dlt698.rs485.service.impl;

import cn.com.wenyl.bs.dlt698.common.constants.RS485;
import cn.com.wenyl.bs.dlt698.rs485.service.RS485Service;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import cn.com.wenyl.bs.dlt698.utils.SerialCommUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service("rs485ServiceImpl")
@Slf4j
public class RS485ServiceImpl implements RS485Service {
    @Override
    public byte[] sendByte(byte[] data) throws RuntimeException, TimeoutException,ExecutionException, InterruptedException {
        SerialCommUtils serial = SerialCommUtils.getInstance();
        CompletableFuture<ResponseEntity<byte[]>> future = new CompletableFuture<>();
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
            log.info("发送数据帧-{}", HexUtils.bytesToHex(data));
            serial.sendData(data);
            return future.get(5, TimeUnit.SECONDS).getBody();
        }catch (TimeoutException e) {
            log.error("rs485通信等待超时");
            future.completeExceptionally(e);
            throw e; // 超时
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();  // 恢复中断状态
            future.completeExceptionally(e);
            throw e; // 其他异常
        } finally {
            serial.closePort();
        }
    }
}

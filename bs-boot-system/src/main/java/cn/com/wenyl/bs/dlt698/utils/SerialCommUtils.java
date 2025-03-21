package cn.com.wenyl.bs.dlt698.utils;



import cn.com.wenyl.bs.dlt698.constants.DLT698Def;
import cn.com.wenyl.bs.dlt698.constants.RS485;
import cn.com.wenyl.bs.dlt698.entity.LengthDomain;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Slf4j
@Data
public class SerialCommUtils {
    private ByteArrayOutputStream receiveBuffer = new ByteArrayOutputStream(); // 新增接收缓冲区
    private static volatile SerialCommUtils instance;
    private SerialPort serialPort;
    private DataReceivedListener dataListener;

    public static SerialCommUtils getInstance(){
        if (instance == null) {
            synchronized (SerialCommUtils.class) {
                if (instance == null) {
                    instance = new SerialCommUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 数据接收回调接口
     */
    public interface DataReceivedListener {
        void onDataReceived(byte[] data);
    }

    /**
     * 打开串口
     * @return true 打开成功，false 失败
     */
    public boolean openPort() {
        serialPort = SerialPort.getCommPort(RS485.PART_NAME);
        if (!serialPort.openPort()) {
            log.error("串口打开失败: {}",RS485.PART_NAME);
            return false;
        }
        // 配置串口参数
        serialPort.setBaudRate(RS485.BAUD_RATE);
        serialPort.setNumDataBits(RS485.DATA_BITS);
        serialPort.setNumStopBits(RS485.STOP_BITS);
        serialPort.setParity(RS485.PARITY);

        // 设置数据监听
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                byte[] receivedData = event.getReceivedData(); // 直接获取数据
                if(receivedData != null && receivedData.length > 0) {
                    receiveBuffer.write(receivedData, 0, receivedData.length);
                    // 解析缓冲区中的完整帧
                    getFrame();
                }

            }
        });
        log.info("串口已打开: {}",RS485.PART_NAME);
        return true;

    }

    private int parseLengthDomain(byte[] lengthBytes){
        return (ByteBuffer.wrap(new byte[]{lengthBytes[1],lengthBytes[0] }).getShort() & 0x3FFF) + 2;
    }
    /**
     * 获取帧数据 0x68开头0x16结尾
     */
    private void getFrame() {
        byte[] buffer = receiveBuffer.toByteArray();
        if (buffer.length == 0) {
            return;
        }

        int startIndex = -1;
        int endIndex = -1;
        int frameLength = -1;

        // 查找帧头 0x68
        for (int i = 0; i < buffer.length - 2; i++) {
            if (buffer[i] == (byte) 0x68) {
                startIndex = i;

                // 确保至少有 3 个字节可用（0x68 + 长度 + 其他数据）
                if (i + 2 < buffer.length) {
                    byte[] lengthByte = new byte[2];
                    lengthByte[0] = buffer[i + 1];
                    lengthByte[1] = buffer[i + 2];
                    frameLength = this.parseLengthDomain(lengthByte);
                    endIndex = startIndex + frameLength-1; // 计算正确的帧结束位置
                    break;
                }
            }
        }

        if (startIndex == -1) {
            log.error("无效帧数据: {},重置缓冲区",HexUtils.bytesToHex(buffer));
//            receiveBuffer.reset();
            return;
        }

        if (endIndex == -1 || endIndex >= buffer.length) {
            log.info("数据不完整，等待更多数据");
            return; // 数据不完整，等待更多数据
        }
        if (buffer[endIndex] == (byte) 0x16) {
            // 取出完整帧
            byte[] frame = Arrays.copyOfRange(buffer, startIndex, endIndex + 1);

            // 触发回调
            if (dataListener != null) {
                dataListener.onDataReceived(frame);
            }

            // 移除已处理的数据
            byte[] remaining = Arrays.copyOfRange(buffer, endIndex + 1, buffer.length);
            receiveBuffer.reset();
            receiveBuffer.write(remaining, 0, remaining.length);

            // 递归检查剩余数据
            getFrame();
        } else {
            // 帧结束符不正确
            log.error("帧尾错误: {}", HexUtils.bytesToHex(buffer));
            receiveBuffer.reset();
        }
    }


    /**
     * 关闭串口
     */
    public void closePort() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            log.info("串口已关闭: " + RS485.PART_NAME);
        }
    }

    /**
     * 发送数据（字节数组）
     * @param data 待发送的字节数组
     */
    public void sendData(byte[] data) throws RuntimeException{
        if (serialPort == null || !serialPort.isOpen()) {
            log.error("串口未打开");
            throw new RuntimeException("串口未打开");
        }
        serialPort.writeBytes(data, data.length);
    }


    /**
     * 设置数据接收监听器
     * @param listener 数据接收回调接口
     */
    public void setDataReceivedListener(DataReceivedListener listener) {
        this.dataListener = listener;
    }



}

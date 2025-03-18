package cn.com.wenyl.bs.dlt698.utils;



import cn.com.wenyl.bs.dlt698.constants.RS485;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
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
    // 数据接收回调接口
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
            log.error("串口打开失败: " + RS485.PART_NAME);
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

        System.out.println("串口已打开: " + RS485.PART_NAME);
        return true;

    }
    private void getFrame() {
        byte[] buffer = receiveBuffer.toByteArray();
        if(buffer.length == 0){
            return;
        }
        int startIndex = -1;
        int endIndex = -1;

        // 查找帧头 0x68
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == (byte) 0x68) {
                startIndex = i;
            }
            if (buffer[i] == (byte) 0x16) {
                endIndex = i;
                break;
            }
        }
        if(startIndex == -1){
            System.out.println("无效帧数据:"+ HexUtils.bytesToHex(buffer));
            receiveBuffer.reset();
            return;
        }
        if(endIndex == -1){
            return;
        }
        if(endIndex > startIndex) {
            // 提取完整帧
            byte[] frame = Arrays.copyOfRange(buffer, startIndex, endIndex+1);

            // 触发回调
            if (dataListener != null) {
                dataListener.onDataReceived(frame);
            }

            // 移除已处理的数据
            byte[] remaining = Arrays.copyOfRange(buffer, endIndex+1, buffer.length);
            receiveBuffer.reset();
            receiveBuffer.write(remaining, 0, remaining.length);

            // 递归检查剩余数据是否包含其他帧
            getFrame();
        }else{
            // 未找到帧头，清空无效数据
            System.out.println("帧头与帧尾标识异常"+HexUtils.bytesToHex(buffer));
            receiveBuffer.reset();
        }
    }

    /**
     * 关闭串口
     */
    public void closePort() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("串口已关闭: " + RS485.PART_NAME);
        }
    }

    /**
     * 发送数据（字节数组）
     * @param data 待发送的字节数组
     * @return 实际发送的字节数，-1 表示失败
     */
    public int sendData(byte[] data) {
        if (serialPort == null || !serialPort.isOpen()) {
            System.err.println("串口未打开");
            return -1;
        }
        return serialPort.writeBytes(data, data.length);
    }


    /**
     * 设置数据接收监听器
     * @param listener 数据接收回调接口
     */
    public void setDataReceivedListener(DataReceivedListener listener) {
        this.dataListener = listener;
    }



}

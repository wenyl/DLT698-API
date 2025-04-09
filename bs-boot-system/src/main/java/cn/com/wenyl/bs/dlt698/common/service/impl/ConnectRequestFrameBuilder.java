package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.ConnectInfo;
import cn.com.wenyl.bs.dlt698.common.entity.ConnectRequestData;
import cn.com.wenyl.bs.dlt698.common.entity.ConnectRequestFrame;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service("connectRequestBuilder")
public class ConnectRequestFrameBuilder implements BaseFrameBuilder<ConnectRequestFrame> {

    @Override
    public byte[] buildFrame(ConnectRequestFrame frame) {
        byte[] frameHead = FrameBuildUtils.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);
        return FrameBuildUtils.buildFrame(frameHead,linkUserData);
    }

    @Override
    public byte[] buildLinkUserData(ConnectRequestFrame frame) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        ConnectRequestData userData = frame.getConnectRequestData();
        buffer.put(userData.getApdu());
        buffer.put(userData.getPIID());
        buffer.put(ConnectInfo.PROTOCOL_VERSION);
        buffer.put(ConnectInfo.PROTOCOL_CONFORMANCE);
        buffer.put(ConnectInfo.FUNCTION_CONFORMANCE);
        buffer.put(ConnectInfo.MAX_SEND_PER_FRAME);
        buffer.put(ConnectInfo.MAX_RECEIVE_PER_FRAME);
        buffer.put(ConnectInfo.MAX_FRAME_NUM);
        buffer.put(ConnectInfo.MAX_APDU_SIZE);
        buffer.put(ConnectInfo.CONNECT_TIMEOUT);
        buffer.put(ConnectInfo.SECURITY_TYPE);
        buffer.put(userData.getTimeTag());
        byte[] userDataBytes = new byte[buffer.position()];
        buffer.rewind(); // 重置位置到0
        buffer.get(userDataBytes); // 从缓冲区读取到数组中
        return userDataBytes;
    }
}
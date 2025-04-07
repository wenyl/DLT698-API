package cn.com.wenyl.bs.dlt698.client.service.impl;

import cn.com.wenyl.bs.dlt698.client.entity.ConnectInfo;
import cn.com.wenyl.bs.dlt698.client.entity.ConnectRequestData;
import cn.com.wenyl.bs.dlt698.client.entity.ConnectRequestFrame;
import cn.com.wenyl.bs.dlt698.client.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.client.service.LengthDomainBuildService;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service("connectRequestBuilder")
public class ConnectRequestFrameBuilder extends BaseFrameBuilderImpl<ConnectRequestFrame> implements BaseFrameBuilder<ConnectRequestFrame> {

    public ConnectRequestFrameBuilder() {
        super(ConnectRequestFrame.class);
    }
    @Override
    public byte[] buildFrame(ConnectRequestFrame frame) {
        byte[] frameHead = super.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);
        return super.buildFrame(frameHead,linkUserData);
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
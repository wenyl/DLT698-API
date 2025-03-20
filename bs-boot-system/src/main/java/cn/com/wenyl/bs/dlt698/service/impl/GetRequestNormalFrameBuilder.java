package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.*;
import cn.com.wenyl.bs.dlt698.entity.*;
import cn.com.wenyl.bs.dlt698.service.BaseFrameBuilder;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service("getRequestNormalFrameBuilder")
public class GetRequestNormalFrameBuilder extends BaseFrameBuilderImpl<GetRequestNormalFrame> implements BaseFrameBuilder<GetRequestNormalFrame> {
    public GetRequestNormalFrameBuilder() {
        super(GetRequestNormalFrame.class);
    }
    @Override
    public byte[] buildFrame(GetRequestNormalFrame frame) {
        byte[] frameHead = super.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);
        return super.buildFrame(frameHead,linkUserData);
    }

    @Override
    public byte[] buildLinkUserData(GetRequestNormalFrame frame) {
        GetRequestNormalData userData = frame.getData();
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.put(userData.getApdu());
        buffer.put(userData.getOpera());
        buffer.put(userData.getPIID());
        buffer.put(userData.getOad());
        buffer.put(userData.getTimeTag());
        byte[] ret = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(ret);
        return ret;
    }

}

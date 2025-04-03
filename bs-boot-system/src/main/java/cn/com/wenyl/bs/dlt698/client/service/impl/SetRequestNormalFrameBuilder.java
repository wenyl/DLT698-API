package cn.com.wenyl.bs.dlt698.client.service.impl;

import cn.com.wenyl.bs.dlt698.client.entity.SetRequestNormalData;
import cn.com.wenyl.bs.dlt698.client.entity.SetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.client.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.common.BaseFrameBuilderImpl;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service("setRequestNormalFrameBuilder")
public class SetRequestNormalFrameBuilder extends BaseFrameBuilderImpl<SetRequestNormalFrame> implements BaseFrameBuilder<SetRequestNormalFrame> {
    public SetRequestNormalFrameBuilder() {
        super(SetRequestNormalFrame.class);
    }
    @Override
    public byte[] buildFrame(SetRequestNormalFrame frame) {
        byte[] frameHead = super.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);
        return super.buildFrame(frameHead,linkUserData);
    }

    @Override
    public byte[] buildLinkUserData(SetRequestNormalFrame frame) {
        SetRequestNormalData userData = frame.getData();
        ByteBuffer buffer = ByteBuffer.allocate(64+userData.getDataItem().length);
        buffer.put(userData.getApdu());
        buffer.put(userData.getOpera());
        buffer.put(userData.getPIID());
        buffer.put(userData.getOad());
        buffer.put(userData.getDataItem());
        buffer.put(userData.getTimeTag());
        byte[] ret = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(ret);
        return ret;
    }

}

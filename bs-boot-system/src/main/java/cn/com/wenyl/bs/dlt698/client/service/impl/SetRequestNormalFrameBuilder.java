package cn.com.wenyl.bs.dlt698.client.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.SetRequestNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.SetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service("setRequestNormalFrameBuilder")
public class SetRequestNormalFrameBuilder implements BaseFrameBuilder<SetRequestNormalFrame> {
    @Override
    public byte[] buildFrame(SetRequestNormalFrame frame) {
        byte[] frameHead = FrameBuildUtils.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);
        return FrameBuildUtils.buildFrame(frameHead,linkUserData);
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

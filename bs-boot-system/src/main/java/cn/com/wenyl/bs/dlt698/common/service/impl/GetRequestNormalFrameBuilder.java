package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.common.entity.GetRequestNormalFrame;


import cn.com.wenyl.bs.dlt698.common.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service("getRequestNormalFrameBuilder")
public class GetRequestNormalFrameBuilder implements BaseFrameBuilder<GetRequestNormalFrame> {
    @Override
    public byte[] buildFrame(GetRequestNormalFrame frame) {
        byte[] frameHead = FrameBuildUtils.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);
        return FrameBuildUtils.buildFrame(frameHead,linkUserData);
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

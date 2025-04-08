package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseData;
import cn.com.wenyl.bs.dlt698.common.entity.LinkResponseFrame;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.utils.ASN1EncoderUtils;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Slf4j
@Service("linkResponseFrameBuilder")
public class LinkResponseFrameBuilder implements BaseFrameBuilder<LinkResponseFrame> {
    @Override
    public byte[] buildFrame(LinkResponseFrame frame) {
        byte[] frameHead = FrameBuildUtils.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);
        return FrameBuildUtils.buildFrame(frameHead,linkUserData);
    }

    @Override
    public byte[] buildLinkUserData(LinkResponseFrame frame) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        LinkResponseData data = frame.getLinkUserData();
        buffer.put(data.getApdu());
        buffer.put(data.getPIID());
        buffer.put(data.getResult());
        buffer.put(data.getRequestTimeBytes());
        buffer.put(data.getReceiveTimeBytes());
        data.setResponseTimeBytes(ASN1EncoderUtils.encodeDateTime());
        buffer.put(data.getResponseTimeBytes());
        byte[] ret = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(ret);
        return ret;
    }
}

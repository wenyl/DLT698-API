package cn.com.wenyl.bs.dlt698.common.service.impl;

import cn.com.wenyl.bs.dlt698.common.entity.ProxyTransCommandRequestData;
import cn.com.wenyl.bs.dlt698.common.entity.ProxyTransCommandRequestFrame;
import cn.com.wenyl.bs.dlt698.common.service.BaseFrameBuilder;
import cn.com.wenyl.bs.dlt698.net4g.service.FrameParseProcessor;
import cn.com.wenyl.bs.dlt698.net4g.tcp.DeviceChannelManager;
import cn.com.wenyl.bs.dlt698.utils.FrameBuildUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
@Slf4j
@Service
public class ProxyTransCommandRequestFrameBuilder implements BaseFrameBuilder<ProxyTransCommandRequestFrame> {

    @Override
    public byte[] buildFrame(ProxyTransCommandRequestFrame frame) {
        byte[] frameHead = FrameBuildUtils.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);
        return FrameBuildUtils.buildFrame(frameHead,linkUserData);
    }

    @Override
    public byte[] buildLinkUserData(ProxyTransCommandRequestFrame frame) {
        ProxyTransCommandRequestData userData = frame.getData();
        ByteBuffer buffer = ByteBuffer.allocate(128);
        buffer.put(userData.getApdu());
        buffer.put(userData.getOpera());
        buffer.put(userData.getPIID());
        buffer.put(userData.getOad());
        buffer.put(userData.getComDcb());
        buffer.put(userData.getUsRevTimeout());
        buffer.put(userData.getUsByteTimeout());
        buffer.put(userData.getAucCmd());
        buffer.put(userData.getTimeTag());
        byte[] ret = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(ret);
        return ret;
    }
}

package cn.com.wenyl.bs.dlt698.service.impl;


import cn.com.wenyl.bs.dlt698.entity.Frame;
import cn.com.wenyl.bs.dlt698.service.*;

import javax.annotation.Resource;
import java.nio.ByteBuffer;

public abstract class BaseFrameBuilderImpl<T extends Frame>  implements BaseFrameBuilder<T> {
    @Resource
    private ControlDomainBuildService controlDomainBuildService;
    @Resource
    private AddressDomainBuildService addressDomainBuildService;
    @Resource
    private LengthDomainBuildService lengthDomainBuildService;
    @Resource
    private CheckBuildService checkBuildService;
    public byte[] buildFrameHead(Frame frame) {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put((byte)0);
        buffer.put((byte)0);
        // 长度域
        buffer.put(controlDomainBuildService.buildControlDomain(frame.getControlDomain()));
        // 地址域
        buffer.put(addressDomainBuildService.buildAddressDomain(frame.getAddressDomain()));
        byte[] byteArray = new byte[buffer.position()];
        buffer.rewind(); // 重置位置到0
        buffer.get(byteArray); // 从缓冲区读取到数组中
        return byteArray;
    }
    public abstract byte[] buildFrame(T frame);
    public abstract byte[] buildLinkUserData(T frame);

    public byte[] buildFrameLength(int length) {
        return lengthDomainBuildService.buildFrameLength(length);
    }
    public byte[] buildHCS(byte[] frameHead){
        return checkBuildService.buildHCS(frameHead);
    }
    public byte[] buildFCS(byte[] frameBody){
        return checkBuildService.buildFCS(frameBody);
    }
}

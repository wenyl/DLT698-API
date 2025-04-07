package cn.com.wenyl.bs.dlt698.client.service.impl;


import cn.com.wenyl.bs.dlt698.client.constants.DLT698Def;
import cn.com.wenyl.bs.dlt698.client.constants.RequestType;
import cn.com.wenyl.bs.dlt698.client.service.*;
import cn.com.wenyl.bs.dlt698.common.AddressDomain;
import cn.com.wenyl.bs.dlt698.common.ControlDomain;
import cn.com.wenyl.bs.dlt698.common.Frame;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public abstract class BaseFrameBuilderImpl<T extends Frame>  implements BaseFrameBuilder<T> {
    private final Class<T> type;
    public BaseFrameBuilderImpl(Class<T> type){
        this.type = type;
    }
    @Resource
    private ControlDomainBuildService controlDomainBuildService;
    @Resource
    private AddressDomainBuildService addressDomainBuildService;
    @Resource
    private LengthDomainBuildService lengthDomainBuildService;
    @Resource
    private CheckBuildService checkBuildService;

    @Override
    public byte[] buildFrame(byte[] frameHead, byte[] linkUserData) {
        // 构建长度域，并初始化帧头，数据长度为帧头+apdu链路用户数据长度+2字节hcs校验信息+2字节fcs校验信息
        byte[] lengthDomain = this.buildFrameLength(frameHead.length+linkUserData.length+4);
        frameHead[0] = lengthDomain[0];
        frameHead[1] = lengthDomain[1];
        byte[] hcs = this.buildHCS(frameHead);

        byte[] body = new byte[frameHead.length+2+linkUserData.length];
        System.arraycopy(frameHead, 0, body, 0, frameHead.length);
        System.arraycopy(hcs, 0, body, frameHead.length, hcs.length);
        System.arraycopy(linkUserData, 0, body, frameHead.length+hcs.length, linkUserData.length);

        byte[] fcs = this.buildFCS(body);

        byte[] totalFrameData = new byte[body.length+fcs.length+2];
        totalFrameData[0] = DLT698Def.START_MARK;
        System.arraycopy(body, 0, totalFrameData, 1, body.length);
        System.arraycopy(fcs, 0, totalFrameData, body.length+1, fcs.length);
        totalFrameData[totalFrameData.length-1] = DLT698Def.END_MARK;
        return totalFrameData;
    }

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
    @Override
    public Frame getFrame(int functionCode, int scramblingCodeFlag,
                          int frameFlag, RequestType requestType, int addressType,
                          int logicAddress, byte[] serverAddress, byte clientAddress
    ){
        T t;
        try {
            t = type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("无法创建实例", e);
        }
        ControlDomain controlDomain = new ControlDomain();
        controlDomain.setFunCode(functionCode);
        controlDomain.setSc(scramblingCodeFlag);
        controlDomain.setFrameFlg(frameFlag);
        controlDomain.setPrm(requestType.getPrm());
        controlDomain.setDir(requestType.getDir());
        t.setControlDomain(controlDomain);

        AddressDomain addressDomain = new AddressDomain();
        addressDomain.setAddressType(addressType);
        addressDomain.setLogicAddress(logicAddress);
        addressDomain.setServerAddress(serverAddress);
        addressDomain.setClientAddress(clientAddress);
        t.setAddressDomain(addressDomain);
        return t;
    }
    public abstract byte[] buildLinkUserData(T frame);
    @Override
    public byte[] buildFrameLength(int length) {
        return lengthDomainBuildService.buildFrameLength(length);
    }
    @Override
    public byte[] buildHCS(byte[] frameHead){
        return checkBuildService.buildHCS(frameHead);
    }
    @Override
    public byte[] buildFCS(byte[] frameBody){
        return checkBuildService.buildFCS(frameBody);
    }


}

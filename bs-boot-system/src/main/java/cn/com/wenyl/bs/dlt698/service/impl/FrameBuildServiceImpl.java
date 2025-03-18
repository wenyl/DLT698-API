package cn.com.wenyl.bs.dlt698.service.impl;



import cn.com.wenyl.bs.dlt698.constants.DLT698Def;
import cn.com.wenyl.bs.dlt698.entity.APDU;
import cn.com.wenyl.bs.dlt698.entity.CSInfo;
import cn.com.wenyl.bs.dlt698.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;

@Service("frameBuildService")
public class FrameBuildServiceImpl implements FrameBuildService {
    @Resource
    private UserDataBuildService userDataBuildService;
    @Resource
    private ControlDomainBuildService controlDomainBuildService;
    @Resource
    private AddressDomainBuildService addressDomainBuildService;
    @Resource
    private CheckBuildService checkBuildService;
    @Autowired
    private LengthDomainBuildServiceImpl lengthDomainBuildService;

    @Override
    public byte[] buildFrameHead(CSInfo csInfo) {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        // 长度域2个字节
        buffer.put((byte)0);
        buffer.put((byte)0);

        // 控制域1个字节
        buffer.put(controlDomainBuildService.buildControlDomain(csInfo));

        // 地址域
        buffer.put(addressDomainBuildService.buildAddressDomain(csInfo));

        byte[] byteArray = new byte[buffer.position()];
        buffer.rewind(); // 重置位置到0
        buffer.get(byteArray); // 从缓冲区读取到数组中
        return byteArray;
    }

    @Override
    public byte[] buildUserData(byte[] apdu) {
        return new byte[0];
    }

    @Override
    public byte[] buildFrame(CSInfo csInfo, APDU apdu) {
        // 帧头数据，不包含起始符，长度域尚未初始化
        byte[] frameHead = this.buildFrameHead(csInfo);
        // 构建apdu链路用户数据
        byte[] userData = userDataBuildService.buildUserData(apdu);
        // 构建长度域，并初始化帧头，数据长度为帧头+apdu链路用户数据长度+2字节hcs校验信息+2字节fcs校验信息
        byte[] lengthDomain = lengthDomainBuildService.buildFrameLength(frameHead.length+userData.length+4);
        frameHead[0] = lengthDomain[0];
        frameHead[1] = lengthDomain[1];

        // 生成HCS信息
        byte[] hcsFrame = new byte[frameHead.length+2];
        System.arraycopy(frameHead, 0, hcsFrame, 0, frameHead.length);
        checkBuildService.buildHCS(hcsFrame,frameHead.length);

        // 生成FCS信息
        byte[] fcsFrame = new byte[hcsFrame.length+userData.length+2];
        System.arraycopy(hcsFrame, 0, fcsFrame, 0, hcsFrame.length);
        System.arraycopy(userData, 0, fcsFrame, hcsFrame.length, userData.length);

        checkBuildService.buildFCS(fcsFrame,hcsFrame.length+userData.length);

        byte[] frame = new byte[fcsFrame.length+2];
        frame[0] = DLT698Def.START_MARK;
        System.arraycopy(fcsFrame, 0, frame, 1, fcsFrame.length);
        frame[fcsFrame.length+1] = DLT698Def.END_MARK;
        return frame;
    }


}

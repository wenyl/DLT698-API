package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.constants.DLT698Def;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalData;
import cn.com.wenyl.bs.dlt698.entity.GetRequestNormalFrame;
import cn.com.wenyl.bs.dlt698.service.BaseFrameBuilder;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service("getRequestNormalFrameBuilder")
public class GetRequestNormalFrameBuilder extends BaseFrameBuilderImpl<GetRequestNormalFrame> implements BaseFrameBuilder<GetRequestNormalFrame> {

    @Override
    public byte[] buildFrame(GetRequestNormalFrame frame) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        byte[] frameHead = super.buildFrameHead(frame);
        byte[] linkUserData = buildLinkUserData(frame);

        // 构建长度域，并初始化帧头，数据长度为帧头+apdu链路用户数据长度+2字节hcs校验信息+2字节fcs校验信息
        byte[] lengthDomain = super.buildFrameLength(frameHead.length+linkUserData.length+4);
        frameHead[0] = lengthDomain[0];
        frameHead[1] = lengthDomain[1];
        byte[] hcs = super.buildHCS(frameHead);

        byte[] body = new byte[frameHead.length+2+linkUserData.length];
        System.arraycopy(frameHead, 0, body, 0, frameHead.length);
        System.arraycopy(hcs, 0, body, frameHead.length, hcs.length);
        System.arraycopy(linkUserData, 0, body, frameHead.length+hcs.length, linkUserData.length);

        byte[] fcs = super.buildFCS(body);

        byte[] totalFrameData = new byte[body.length+fcs.length+2];
        totalFrameData[0] = DLT698Def.START_MARK;
        System.arraycopy(body, 0, totalFrameData, 1, body.length);
        System.arraycopy(fcs, 0, totalFrameData, body.length+1, fcs.length);
        totalFrameData[totalFrameData.length-1] = DLT698Def.END_MARK;
        return totalFrameData;
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


//    @Override
//    public byte[] buildFrame(CSInfo csInfo, APDU apdu) {
//        // 帧头数据，不包含起始符，长度域尚未初始化
//        byte[] frameHead = this.buildFrameHead(csInfo);
//        // 构建apdu链路用户数据
//        byte[] userData = userDataBuildService.buildUserData(apdu);
//
//        byte[] lengthDomain = lengthDomainBuildService.buildFrameLength(frameHead.length+userData.length+4);
//        frameHead[0] = lengthDomain[0];
//        frameHead[1] = lengthDomain[1];
//
//        // 生成HCS信息
//        byte[] hcsFrame = new byte[frameHead.length+2];
//        System.arraycopy(frameHead, 0, hcsFrame, 0, frameHead.length);
//        checkBuildService.buildHCS(hcsFrame,frameHead.length);
//
//        // 生成FCS信息
//        byte[] fcsFrame = new byte[hcsFrame.length+userData.length+2];
//        System.arraycopy(hcsFrame, 0, fcsFrame, 0, hcsFrame.length);
//        System.arraycopy(userData, 0, fcsFrame, hcsFrame.length, userData.length);
//
//        checkBuildService.buildFCS(fcsFrame,hcsFrame.length+userData.length);
//
//        byte[] frame = new byte[fcsFrame.length+2];
//        frame[0] = DLT698Def.START_MARK;
//        System.arraycopy(fcsFrame, 0, frame, 1, fcsFrame.length);
//        frame[fcsFrame.length+1] = DLT698Def.END_MARK;
//        return frame;
//    }

}

package cn.com.wenyl.bs.dlt698.client.service.impl;


import cn.com.wenyl.bs.dlt698.client.service.CheckBuildService;
import cn.com.wenyl.bs.dlt698.utils.FrameCheckUtils;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service("checkBuildService")
public class CheckBuildServiceImpl implements CheckBuildService {
    @Override
    public byte[] buildHCS(byte[] headFrame) {
        ByteBuffer buffer = ByteBuffer.allocate(headFrame.length+2);
        buffer.put(headFrame);
        return FrameCheckUtils.tryCS16(buffer.array(), headFrame.length);
    }


    @Override
    public byte[] buildFCS(byte[] bodyFrame) {
        ByteBuffer buffer = ByteBuffer.allocate(bodyFrame.length+2);
        buffer.put(bodyFrame);
        return FrameCheckUtils.tryCS16(buffer.array(), bodyFrame.length);
    }
}

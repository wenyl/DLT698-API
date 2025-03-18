package cn.com.wenyl.bs.dlt698.service.impl;


import cn.com.wenyl.bs.dlt698.service.LengthDomainBuildService;
import org.springframework.stereotype.Service;

@Service("lengthDomainBuildService")
public class LengthDomainBuildServiceImpl implements LengthDomainBuildService {

    @Override
    public byte[] buildFrameLength(int length) {
        byte[] bytes = new byte[2];
        // 低字节 (LSB)
        bytes[0] = (byte)(length & 0xFF);
        // 高字节 (MSB)
        bytes[1] = (byte)((length >> 8) & 0xFF);
        return bytes;
    }
}

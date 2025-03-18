package cn.com.wenyl.bs.dlt698.service.impl;


import cn.com.wenyl.bs.dlt698.entity.CSInfo;
import cn.com.wenyl.bs.dlt698.service.ControlDomainBuildService;
import org.springframework.stereotype.Service;

@Service("controlDomainBuildService")
public class ControlDomainBuildServiceImpl implements ControlDomainBuildService {

    @Override
    public byte buildControlDomain(CSInfo csinfo) {
        byte ctrl = 0;

        // bit0-bit2: 功能码(0-7)
        ctrl |= (byte)(csinfo.getFunCode() & 0x07);

        // bit3: 扰码标志SC
        ctrl |= (byte)((csinfo.getSc() & 0x01) << 3);

        // bit4: 保留，固定为0

        // bit5: 分帧标识
        ctrl |= (byte)((csinfo.getFrameFlg() & 0x01) << 5);

        // bit6: 启动标志PRM
        ctrl |= (byte)((csinfo.getPrm() & 0x01) << 6);

        // bit7: 传输方向DIR
        ctrl |= (byte)((csinfo.getDir() & 0x01) << 7);

        return (byte)(ctrl & 0xFF);
    }
}

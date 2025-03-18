package cn.com.wenyl.bs.dlt698.service.impl;

import cn.com.wenyl.bs.dlt698.entity.CSInfo;
import cn.com.wenyl.bs.dlt698.service.ControlDomainParseService;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import org.springframework.stereotype.Service;

@Service("controlDomainParseService")
public class ControlDomainParseServiceImpl implements ControlDomainParseService {
    @Override
    public CSInfo parseControlDomain(byte controlDomain) {
        CSInfo csInfo = new CSInfo();
        int controlField = controlDomain & 0xFF;
        int funcCode = controlField & 0x07; // 提取功能码（bit0-bit2）
        int sc = (controlField >>> 3) & 0x01; // 提取扰码标志SC（bit3）
        int frameFlag = (controlField >>> 5) & 0x01; // 分帧标识（bit5）
        int prm = (controlField >>> 6) & 0x01; // 启动标志PRM（bit6）
        int dir = (controlField >>> 7) & 0x01; // 方向标志DIR（bit7）
        csInfo.setFunCode(funcCode);
        csInfo.setSc(sc);
        csInfo.setFrameFlg(frameFlag);
        csInfo.setPrm(prm);
        csInfo.setDir(dir);
        return csInfo;
    }
}

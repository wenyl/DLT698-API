package cn.com.wenyl.bs.dlt698.service;


import cn.com.wenyl.bs.dlt698.entity.CSInfo;

public interface ControlDomainBuildService {
    /**
     * 获取控制字
     * 控制域定义：
     * bit0-bit2: 功能码(0-7)
     * bit3: 扰码标志SC
     * bit4: 保留
     * bit5: 分帧标识
     * bit6: 启动标志PRM
     * bit7: 传输方向DIR
     */
    byte buildControlDomain(CSInfo csinfo);
}

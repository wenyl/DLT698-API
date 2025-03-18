package cn.com.wenyl.bs.dlt698.service;


import cn.com.wenyl.bs.dlt698.entity.APDU;
import cn.com.wenyl.bs.dlt698.entity.CSInfo;

public interface FrameBuildService {
    /**
     * 组装帧头，不包含起始符，长度域尚未初始化
     * @param csInfo 控制域信息
     * @return 帧头数据
     */
    byte[] buildFrameHead(CSInfo csInfo);
    /**
     * 组装用户链路数据
     * @param apdu APDU数据
     * @return 用户链路数据帧
     */
    byte[] buildUserData(byte[] apdu);
    /**
     * 组装完整帧数据
     * @param csInfo APDU数据
     * @return 完整帧数据
     */
    byte[] buildFrame(CSInfo csInfo, APDU apdu);
}

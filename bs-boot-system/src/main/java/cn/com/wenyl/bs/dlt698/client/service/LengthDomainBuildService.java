package cn.com.wenyl.bs.dlt698.client.service;

/**
 * 长度域构建服务
 */
public interface LengthDomainBuildService {

    /**
     * 把十进制长度转化为2字节的长度数据
     * @param length 帧数据长度
     * @return 返回长度域数据
     */
    byte[] buildFrameLength(int length);
}

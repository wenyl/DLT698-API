package cn.com.wenyl.bs.dlt698.client.service;

/**
 * 校验信息服务
 */
public interface CheckBuildService {

    /**
     * 帧头校验HCS为2字节，是对帧头部分不包含起始字符、HCS本身的所有字节的校验
     * @param headFrame 帧头数据，最后两位空余用于存储生成的hcs数据
     * @return 返回校验值
     */
    byte[] buildHCS(byte[] headFrame);
    /**
     * 帧校验FCS为2字节，是对整帧不包含起始字符、结束字符和FCS本身的所有字节的校验
     * @param bodyFrame 帧数据，最后两位空余用于存储生成的fcs数据
     * @return 返回校验值
     */
    byte[] buildFCS(byte[] bodyFrame);
}

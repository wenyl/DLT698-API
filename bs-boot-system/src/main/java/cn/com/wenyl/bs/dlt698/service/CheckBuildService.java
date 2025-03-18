package cn.com.wenyl.bs.dlt698.service;

public interface CheckBuildService {

    /**
     * 帧头校验HCS为2字节，是对帧头部分不包含起始字符、HCS本身的所有字节的校验
     * 根据帧头数据生成hcs校验信息，这个方法要求headFrame空余，用于存储帧头数据，返回的帧头数据仅用于记录，不需要再根据返回值设置frameHead中的hcs信息
     * @param headFrame 帧头数据，最后两位空余用于存储生成的hcs数据
     * @param length 数据实际长度
     */
    void buildHCS(byte[] headFrame,int length);
    /**
     * 帧校验FCS为2字节，是对整帧不包含起始字符、结束字符和FCS本身的所有字节的校验
     * 根据帧头数据生成fcs校验信息，这个方法要求headFrame空余，用于存储帧头数据，返回的帧头数据仅用于记录，不需要再根据返回值设置frameHead中的fcs信息
     * @param bodyFrame 帧数据，最后两位空余用于存储生成的fcs数据
     * @param length 数据实际长度
     */
    void buildFCS(byte[] bodyFrame,int length);
}

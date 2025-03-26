package cn.com.wenyl.bs.dlt698.service;

import cn.com.wenyl.bs.dlt698.constants.DataType;
import cn.com.wenyl.bs.dlt698.entity.*;
import cn.com.wenyl.bs.dlt698.entity.dto.FrameDto;

/**
 * 基础帧解析服务
 * @param <T> 帧数据
 */
public interface BaseFrameParser<T extends Frame,G extends LinkUserData> {
    /**
     * 将帧字节数据解析为帧数据
     * @param frameBytes 帧字节数据
     * @return 帧数据
     * @throws RuntimeException 解析异常
     */
    T parseFrame(byte[] frameBytes) throws RuntimeException;

    /**
     * 提取公用的frameDto数据，不对链路用户数据做任何处理，直接 返回
     * @param frameBytes 完整帧数据
     * @return frameDto数据
     * @throws RuntimeException 异常
     */
    FrameDto getFrameDto(byte[] frameBytes) throws RuntimeException;

    /**
     * 解析帧的用户链路数据
     * @param userDataBytes 用户链路数据字节信息
     * @return 用户链路数据
     */
    G parseLinkUserData(byte[] userDataBytes);

    /**
     * 解析帧的控制域数据
     * @param frameBytes 用户控制域数据字节信息
     * @return 控制域数据
     */
    ControlDomain parseControlDomain(byte frameBytes);

    /**
     * 解析帧的长度域数据
     * @param frameBytes 用户长度域数据字节信息
     * @return 长度域数据
     */
    LengthDomain parseLengthDomain(byte[] frameBytes);

    /**
     * 解析帧的地址域数据
     * @param addressBytes 用户地址域数据字节信息
     * @return 地址域数据
     */
    AddressDomain parseAddressDomain(byte[] addressBytes);

    /**
     * 解析帧头数据
     * @param frameBytes 帧头数据字节信息
     * @return 帧头数据
     */
    FrameDto parseFrameHead(byte[] frameBytes);

    /**
     * 校验帧
     * @param frameBytes 帧数据
     * @return 校验结果
     * @throws RuntimeException 异常信息
     */
    boolean checkFrame(byte[] frameBytes)  throws RuntimeException;

    /**
     * 获取地址域A
     * 服务器地址SA由1字节地址特征和N个字节地址组成，客户机地址占一个字节，0表示不关注客户机地址
     * 1、地址特征定义：
     *   a、bit0…bit3：为地址的字节数，取值范围：0…15，对应表示1…16个字节长度
     *   b、bit4…bit5：逻辑地址；
     *         bit5=0 表示无扩展逻辑地址，bit4取值0和1分别表示逻辑地址0和1；
     *         bit5=1 表示有扩展逻辑地址，bit4备用；地址长度N包含1个字节的扩展逻辑地址，取值范围2…255，表示逻辑地址2…255；
     *   c、bit6…bit7：为服务器地址的地址类型，0 表示单地址，1 表示通配地址，2 表示组地址，3表示广播地址。
     * 2、扩展逻辑地址和地址要求如下：
     *   a、扩展逻辑地址取值范围2…255；
     *   b、编码方式为压缩BCD码，0保留；
     *   c、当服务器地址的十进制位数为奇数时，最后字节的bit3…bit0用FH表示
     * @param frameBytes 完整帧数据
     * @param offsetArray 存储地址域数据在帧数据中的结束位置
     * @return 地址域A
     */
    byte[] getAddressBytes(byte[] frameBytes,int[] offsetArray);

    /**
     * 获取完整帧数据中的HCS
     * @param frameBytes 完整的帧数据
     * @param offset 数组下标
     * @return HCS
     */
    byte[] getHCS(byte[] frameBytes,int offset);
    /**
     * 获取完整帧数据中的FCS
     * @param frameBytes 完整的帧数据
     * @param offset 数组下标
     * @return FCS
     */
    byte[] getFCS(byte[] frameBytes,int offset);

    /**
     * 校验HCS
     * @param frameDto 帧信息
     * @return 返回校验结果
     */
    boolean checkFrameHCS(FrameDto frameDto);

    /**
     * 校验FCS
     * @param frameDto 帧信息
     * @return 返回校验结果
     */
    boolean checkFrameFCS(FrameDto frameDto);

    /**
     * 校验HCS，根据帧头生成hcs与帧自带的hcs对比
     * @param frameHead 帧头
     * @param length 数据长度
     * @param hcs 帧数据中的hcs
     * @return 校验结果
     */
    boolean checkHCS(byte[] frameHead,int length,byte[] hcs);

    /**
     * 校验FCS，根据帧头生成fcs与帧自带的fcs对比
     * @param frameData 帧体数据
     * @param length 长度
     * @param fcs 帧数据中的fcs
     * @return 校验结果
     */
    boolean checkFCS(byte[] frameData,int length,byte[] fcs);

    /**
     * 将OAD字节信息解析为OAD实体
     * @param oad oad字节信息
     * @return OAD实体
     */
    OAD parseOAD(byte[] oad);

    /**
     * 解析帧中的实际数据，这里帧实例中会包含其他数据
     * @param frame 帧信息
     * @return 实际数据
     */
    Object getData(T frame) throws RuntimeException;

    /**
     * 解析数据，这里只包含帧中传输的实际数据部分
     * @param dataType 数据类型
     * @param data 数据字节
     * @return 返回数据解析结果
     * @throws RuntimeException 解析异常
     */
    Object getData(DataType dataType,byte[] data) throws RuntimeException;
}

package cn.com.wenyl.bs.dlt698.common.entity.dto;

import cn.com.wenyl.bs.dlt698.common.entity.AddressDomain;
import cn.com.wenyl.bs.dlt698.common.entity.ControlDomain;
import cn.com.wenyl.bs.dlt698.common.entity.LengthDomain;
import lombok.Data;

/**
 * 用于frame解析临时存储
 */
@Data
public class FrameDto {
    // 解析长度域
    private byte[] lengthBytes;
    private LengthDomain lengthDomain;
    // 解析控制域
    private byte controlByte;
    private ControlDomain controlDomain;
    // 获取地址域的字节数据并解析
    private byte[] addressBytes;
    private AddressDomain addressDomain;
    // 校验信息
    private byte[] hcs;
    // 帧头信息（长度域+控制域+地址域）
    private byte[] frameHeadWithHcsBytes;
    // 校验信息
    private byte[] fcs;
    // 用户数据
    private byte[] userData;
    // 标识当前读取位
    int offset;
}

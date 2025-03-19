package cn.com.wenyl.bs.dlt698.entity;

import lombok.Data;

/**
 * 帧的基础数据
 */
@Data
public class Frame {
    LengthDomain lengthDomain;
    ControlDomain controlDomain;
    AddressDomain addressDomain;
    byte[] hcs;
    byte[] fcs;
}

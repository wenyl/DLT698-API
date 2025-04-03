package cn.com.wenyl.bs.dlt698.common;

import lombok.Data;

/**
 * 地址域数据
 */
@Data
public class AddressDomain {
    private int addressType;    // 地址类型
    private int logicAddress;       // 逻辑地址
    private byte[] serverAddress;      // 服务器地址
    private byte clientAddress;        // 客户机地址
    private int addressLength; // 地址長度
}

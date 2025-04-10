package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 属性索引
 */
@Getter
public enum AttributeIndex {
    UNKNOWN(null,"未知特征索引"),
    ZERO_ZERO((byte)0x00,"特征0 索引0"),
    ZERO_ONE((byte)0x01,"特征0 索引1");
    private final Byte sign;
    private final String desc;
    AttributeIndex(Byte sign, String desc){
        this.sign = sign;
        this.desc = desc;
    }
    public static AttributeIndex getAttributeIndexBySigh(byte sign){
        AttributeIndex[] values = AttributeIndex.values();
        for(AttributeIndex attributeIndex:values) {
            if (attributeIndex.getSign() == sign) {
                return attributeIndex;
            }
        }
        return UNKNOWN;
    }
}

package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 属性编号及类型
 */
@Getter
public enum AttrNum {
    ATTR_4001_02((byte)0x02,"属性编号02,类型：oct-string(8位字节串)");
    private final byte sign;
    private final String desc;
    AttrNum(byte sign, String desc){
        this.sign = sign;
        this.desc = desc;
    }

    public static AttrNum getAttrNumBySign(byte sign){
        AttrNum[] values = AttrNum.values();
        for(AttrNum attrNum:values) {
            if (attrNum.getSign() == sign) {
                return attrNum;
            }
        }
        return null;
    }
}

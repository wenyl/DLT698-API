package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 属性编号及类型
 */
@Getter
public enum AttrNum {
    UNKNOWN(null,"未知属性"),
    ATTR_02((byte)0x02,"属性编号02");
    private final Byte sign;
    private final String desc;
    AttrNum(Byte sign, String desc){
        this.sign = sign;
        this.desc = desc;
    }

    public static AttrNum getAttrNumBySign(byte sign){
        AttrNum[] values = AttrNum.values();
        for(AttrNum attrNum:values) {
            if (attrNum.sign == null) continue;
            if (attrNum.getSign() == sign) {
                return attrNum;
            }
        }
        return UNKNOWN;
    }
}

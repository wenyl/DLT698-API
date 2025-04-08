package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 结果类型
 */
@Getter
public enum GetResultType {
    DATA((byte)0x01,1,"结果类型:(数据)"),
    ERROR((byte)0x00,0,"结果类型:(错误)");
    private final byte sign;
    private final int signNum;
    private final String desc;
    GetResultType(byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static GetResultType getResultTypeBySign(byte sign){
        GetResultType[] values = GetResultType.values();
        for(GetResultType getResultType:values) {
            if (getResultType.getSign() == sign) {
                return getResultType;
            }
        }
        return null;
    }
}

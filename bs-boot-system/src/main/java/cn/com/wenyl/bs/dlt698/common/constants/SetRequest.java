package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 设置请求
 */
@Getter
public enum SetRequest {
    SET_REQUEST_NORMAL((byte)0x01,1,"请求设置一个对象属性"),
    UNKNOWN(null,-1,"未知的设置请求");
    private final Byte sign;
    private final int signNum;
    private final String desc;
    SetRequest(Byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static SetRequest getSetRequestBySign(Byte sign){
        SetRequest[] values = SetRequest.values();
        for(SetRequest setRequest:values) {
            if (setRequest.getSign() == sign) {
                return setRequest;
            }
        }
        return UNKNOWN;
    }
}

package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 读取响应
 */
@Getter
public enum SetResponse {
    SET_RESPONSE_NORMAL((byte)0x01,1,"设置一个对象属性的响应"),
    UNKNOWN(null,-1,"未知的响应");
    private final Byte sign;
    private final int signNum;
    private final String desc;
    SetResponse(Byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static SetResponse getSetResponseBySign(byte sign){
        SetResponse[] values = SetResponse.values();
        for(SetResponse setResponse:values) {
            if (setResponse.sign == null) continue;

            if (setResponse.getSign() == sign) {
                return setResponse;
            }
        }
        return UNKNOWN;
    }
}

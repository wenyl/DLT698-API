package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 读取响应
 */
@Getter
public enum SetResponse {
    SET_RESPONSE_NORMAL((byte)0x01,1,"设置一个对象属性的响应");
    private final byte sign;
    private final int signNum;
    private final String desc;
    SetResponse(byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static SetResponse getSetResponseBySign(byte sign){
        SetResponse[] values = SetResponse.values();
        for(SetResponse setResponse:values) {
            if (setResponse.getSign() == sign) {
                return setResponse;
            }
        }
        return null;
    }
}

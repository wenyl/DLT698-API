package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 设置请求
 */
@Getter
public enum SetRequest {
    SET_REQUEST_NORMAL((byte)0x01,1,"请求设置一个对象属性");
    private final byte sign;
    private final int signNum;
    private final String desc;
    SetRequest(byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static SetRequest getSetRequestBySign(byte sign){
        SetRequest[] values = SetRequest.values();
        for(SetRequest setRequest:values) {
            if (setRequest.getSign() == sign) {
                return setRequest;
            }
        }
        return null;
    }
}

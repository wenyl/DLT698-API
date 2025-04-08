package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 读取请求
 */
@Getter
public enum GetRequest {
    GET_REQUEST_NORMAL((byte)0x01,1,"请求读取一个对象属性");
    private final byte sign;
    private final int signNum;
    private final String desc;
    GetRequest(byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static GetRequest getRequestBySign(byte sign){
        GetRequest[] values = GetRequest.values();
        for(GetRequest getRequest:values) {
            if (getRequest.getSign() == sign) {
                return getRequest;
            }
        }
        return null;
    }
}

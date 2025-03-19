package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 读取响应
 */
@Getter
public enum GetResponse {
    GET_RESPONSE_NORMAL((byte)0x01,1,"请求读取一个对象属性");
    private final byte sign;
    private final int signNum;
    private final String desc;
    GetResponse(byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static GetResponse getResponseBySign(byte sign){
        GetResponse[] values = GetResponse.values();
        for(GetResponse getResponse:values) {
            if (getResponse.getSign() == sign) {
                return getResponse;
            }
        }
        return null;
    }
}

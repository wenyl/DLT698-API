package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 读取响应
 */
@Getter
public enum GetResponse {
    GET_RESPONSE_NORMAL((byte)0x01,1,"响应读取一个对象属性"),
    UNKNOWN(null,-1,"未知的响应");
    private final Byte sign;
    private final int signNum;
    private final String desc;
    GetResponse(Byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static GetResponse getResponseBySign(Byte sign){
        GetResponse[] values = GetResponse.values();
        for(GetResponse getResponse:values) {
            if (getResponse.sign == null) continue;

            if (getResponse.getSign() == sign) {
                return getResponse;
            }
        }
        return UNKNOWN;
    }
}

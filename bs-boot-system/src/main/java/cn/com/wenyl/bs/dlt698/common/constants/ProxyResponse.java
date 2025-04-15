package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 读取响应
 */
@Getter
public enum ProxyResponse {
    PROXY_TRANS_COMMAND_RESPONSE((byte)0x07,7,"代理透明转发命令的响应"),
    UNKNOWN(null,-1,"未知的响应");
    private final Byte sign;
    private final int signNum;
    private final String desc;
    ProxyResponse(Byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static ProxyResponse getResponseBySign(Byte sign){
        ProxyResponse[] values = ProxyResponse.values();
        for(ProxyResponse getResponse:values) {
            if (getResponse.sign == null) continue;

            if (getResponse.getSign() == sign) {
                return getResponse;
            }
        }
        return UNKNOWN;
    }
}

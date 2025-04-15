package cn.com.wenyl.bs.dlt698.common.entity;

import cn.com.wenyl.bs.dlt698.common.constants.GetRequest;
import lombok.Getter;

@Getter
public enum ProxyRequest {
    PROXY_TRANS_COMMAND_REQUEST((byte)0x07,7,"请求代理透明转发命令"),
    UNKNOWN(null,-1,"未知请求");
    private final Byte sign;
    private final int signNum;
    private final String desc;
    ProxyRequest(Byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static ProxyRequest getProxyRequestBySign(byte sign){
        ProxyRequest[] values = ProxyRequest.values();
        for(ProxyRequest proxyRequest:values) {
            if (proxyRequest.sign == null) continue;
            if (proxyRequest.getSign() == sign) {
                return proxyRequest;
            }
        }
        return UNKNOWN;
    }
}

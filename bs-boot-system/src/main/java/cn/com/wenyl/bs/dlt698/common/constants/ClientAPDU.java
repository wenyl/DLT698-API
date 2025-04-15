package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 客户机应用层数据单元
 */
@Getter
public enum ClientAPDU {
    LINK_REQUEST((byte)0x01,1,"预连接请求"),//link_request本来是属于LINK-APDU，这里为了方便管理，统一在ClientAPDU中管理
    CONNECT_REQUEST((byte)0x02,2,"建立应用连接请求"),
    GET_REQUEST((byte)0x05,5,"读取请求"),
    SET_REQUEST((byte)0x06,6,"设置请求"),
    PROXY_REQUEST((byte)0x09,9,"代理请求"),
    UNKNOWN(null,-1,"未知请求");
    private final Byte sign;
    private final int signNum;
    private final String desc;
    ClientAPDU(Byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static ClientAPDU getClientAPDUBySign(byte sign){
        ClientAPDU[] values = ClientAPDU.values();
        for(ClientAPDU clientAPDU:values) {
            if (clientAPDU.sign == null) continue;
            if (clientAPDU.getSign() == sign) {
                return clientAPDU;
            }
        }
        return UNKNOWN;
    }
}

package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 服务器应用层数据单元
 */
@Getter
public enum ServerAPDU {
    LINK_RESPONSE((byte)0x81,129,"预连接响应"),//link_response本来是属于LINK-APDU，这里为了方便管理，统一在ServerAPDU中管理
    CONNECT_RESPONSE((byte)0x82,130,"建立应用连接响应"),
    GET_RESPONSE((byte)0x85,133,"响应读取"),
    SET_RESPONSE((byte)0x86,134,"响应设置"),
    UNKNOWN(null,-1,"未知响应");
    private final Byte sign;
    private final int signNum;
    private final String desc;
    ServerAPDU(Byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static ServerAPDU getServerAPDUBySign(byte sign){
        ServerAPDU[] values = ServerAPDU.values();
        for(ServerAPDU serverAPDU:values) {
            if (serverAPDU.getSign() == sign) {
                return serverAPDU;
            }
        }
        return UNKNOWN;
    }
}

package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 服务器应用层数据单元
 */
@Getter
public enum ServerAPDU {
    CONNECT_RESPONSE((byte)0x82,130,"建立应用连接响应"),
    GET_RESPONSE((byte)0x85,133,"响应读取"),
    SET_RESPONSE((byte)0x86,134,"响应设置");
    private final byte sign;
    private final int signNum;
    private final String desc;
    ServerAPDU(byte sign, int signNum, String desc){
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
        return null;
    }
}

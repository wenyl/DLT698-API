package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 服务器应用层数据单元
 */
@Getter
public enum ServerAPDU {
    GET_RESPONSE((byte)0x85,133,"响应读取");
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

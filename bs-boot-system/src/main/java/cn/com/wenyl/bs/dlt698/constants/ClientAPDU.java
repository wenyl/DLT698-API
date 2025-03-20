package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 客户机应用层数据单元
 */
@Getter
public enum ClientAPDU {
    CONNECT_REQUEST((byte)0x02,2,"建立应用连接请求"),
    GET_REQUEST((byte)0x05,5,"读取请求"),
    SET_REQUEST((byte)0x06,6,"设置请求");
    private final byte sign;
    private final int signNum;
    private final String desc;
    ClientAPDU(byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static ClientAPDU getClientAPDUBySign(byte sign){
        ClientAPDU[] values = ClientAPDU.values();
        for(ClientAPDU clientAPDU:values) {
            if (clientAPDU.getSign() == sign) {
                return clientAPDU;
            }
        }
        return null;
    }
}

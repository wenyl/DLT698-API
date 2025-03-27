package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 应用链接请求认证的结果
 */
@Getter
public enum ConnectResult {
    SUCCESS((byte)0x00,0,"允许建立链接"),PWD_ERROR((byte)0x01,1,"密码错误"),SYMMETRIC_DEC_ERROR((byte)0x02,2,"对称解密错误")
    , ASYMMETRIC_DEC_ERROR((byte)0x03,3,"非对称解密错误"),SIGN_ERROR((byte)0x04,4,"签名错误")
    ,PROTOCOL_ERROR((byte)0x05,5,"协议版本错误"),ESAM_ERROR((byte)0x06,6,"ESAM通信错误"),OTHER_ERROR((byte)0xff,255,"其他错误");
    private final byte sign;
    private final int num;
    private final String desc;
    ConnectResult(byte sign, int num,String desc){
        this.sign = sign;
        this.num = num;
        this.desc = desc;
    }

    public static ConnectResult getConnectResultBySign(byte sign){
        ConnectResult[] values = ConnectResult.values();
        for(ConnectResult connectResult:values) {
            if (connectResult.getSign() == sign) {
                return connectResult;
            }
        }
        return null;
    }
}

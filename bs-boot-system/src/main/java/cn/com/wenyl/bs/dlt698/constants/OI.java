package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

@Getter
public enum OI {
    MAIL_ADDRESS(new byte[]{(byte) 0x40, (byte) 0x01},"获取通信地址");
    private final byte[] sign;
    private final String desc;
    OI(byte[] sign, String desc){
        this.sign = sign;
        this.desc = desc;
    }

    public static OI getOIBySign(byte[] sign){
        OI[] values = OI.values();
        for(OI oi:values) {
            byte[] oiSign = oi.sign;
            if (oiSign[0] == sign[0] && oiSign[1] == sign[1]) {
                return oi;
            }
        }
        return null;
    }
}

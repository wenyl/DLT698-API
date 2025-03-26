package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * OI接口地址
 */
@Getter
public enum OI {
    PAEE(new byte[]{(byte) 0x00, (byte) 0x10},"正向有功电能"),
    RAEE(new byte[]{(byte) 0x00, (byte) 0x20},"反向有功电能"),
    VOLTAGE(new byte[]{(byte) 0x20, (byte) 0x00},"电压"),
    ELECTRIC_CURRENT(new byte[]{(byte) 0x20, (byte) 0x01},"电流"),
    FORWARD_CARBON_EMISSION(new byte[]{(byte)0x20,(byte)0x80},"正向碳排放量"),
    REVERSE_CARBON_EMISSION(new byte[]{(byte)0x20,(byte)0x90},"反向碳排放量"),
    MAIL_ADDRESS(new byte[]{(byte) 0x40, (byte) 0x01},"获取通信地址"),
    SET_CARBON_FACTOR(new byte[]{(byte)0x41,(byte)0x20},"设置碳因子");
    // 字节标识
    private final byte[] sign;
    // 描述
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

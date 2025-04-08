package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 错误信息
 */
@Getter
public enum DARType {
    _0((byte)0x00,0,"成功"),_1((byte)0x01,1,"硬件失效"),_2((byte)0x02,2,"暂时失效")
    ,_3((byte)0x03,3,"拒绝读写"),_4((byte)0x04,4,"对象未定义"),_5((byte)0x05,5,"对象接口类不符合")
    ,_6((byte)0x06,6,"对象不存在"),_7((byte)0x07,7,"类型不匹配"),_8((byte)0x08,0,"越界")
    ,_9((byte)0x09,9,"数据块不可用"),_10((byte)0x0A,10,"分帧传输已取消"),_11((byte)0x0B,11,"不处于分帧传输状态")
    ,_12((byte)0x0C,12,"块写取消"),_13((byte)0x0D,13,"不存在块写状态"),_14((byte)0x0E,14,"数据块序号无效")
    ,_15((byte)0x0F,15,"密码错/未授权"),_16((byte)0x10,16,"通信速率不能更改"),_17((byte)0x11,17,"年时区数超")
    ,_18((byte)0x12,18,"日时段数超"),_19((byte)0x13,19,"费率数超"),_20((byte)0x14,20,"安全认证不匹配")
    ,_21((byte)0x15,21,"重复充值"),_22((byte)0x16,22,"ESAM验证失败"),_23((byte)0x17,23,"安全认证失败")
    ,_24((byte)0x18,24,"客户编号不匹配"),_25((byte)0x19,25,"充值次数错误"),_26((byte)0x1A,26,"购电超囤积")
    ,_27((byte)0x1B,27,"地址异常"),_28((byte)0x1C,28,"对称解密错误"),_29((byte)0x1D,29,"非对称解密错误")
        ,_30((byte)0x1E,30,"签名错误"),_31((byte)0x1F,31,"电能表挂起"),_32((byte)0x20,32,"时间标签无效")
    ,_33((byte)0x21,33,"请求超时"),_34((byte)0x22,34,"ESAM的P1P2不正确"),_35((byte)0x23,35,"ESAM的LC错误")
    ,_36((byte)0x24,36,"比对失败"),_255((byte)0xFF,255,"其它");

    private final int sign;
    private final int signNum;
    private final String desc;
    DARType(int sign, int signNum, String desc){
        this.signNum = signNum;
        this.sign = sign;
        this.desc = desc;
    }

    public static DARType getDARBySign(byte sign){
        DARType[] values = DARType.values();
        for(DARType dar:values) {
            if (dar.getSign() == sign) {
                return dar;
            }
        }
        return null;
    }
}

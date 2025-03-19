package cn.com.wenyl.bs.dlt698.constants;

import lombok.Getter;

/**
 * 错误信息
 */
@Getter
public enum Error {
    _0(0,"成功"),_1(1,"硬件失效"),_2(2,"暂时失效")
    ,_3(3,"拒绝读写"),_4(4,"对象未定义"),_5(5,"对象接口类不符合")
    ,_6(6,"对象不存在"),_7(7,"类型不匹配"),_8(0,"越界")
    ,_9(9,"数据块不可用"),_10(10,"分帧传输已取消"),_11(11,"不处于分帧传输状态")
    ,_12(12,"块写取消"),_13(13,"不存在块写状态"),_14(14,"数据块序号无效")
    ,_15(15,"密码错/未授权"),_16(16,"通信速率不能更改"),_17(17,"年时区数超")
    ,_18(18,"日时段数超"),_19(19,"费率数超"),_20(20,"安全认证不匹配")
    ,_21(21,"重复充值"),_22(22,"ESAM验证失败"),_23(23,"安全认证失败")
    ,_24(24,"客户编号不匹配"),_25(25,"充值次数错误"),_26(26,"购电超囤积")
    ,_27(27,"地址异常"),_28(28,"对称解密错误"),_29(29,"非对称解密错误")
    ,_30(30,"签名错误"),_31(31,"电能表挂起"),_32(32,"时间标签无效")
    ,_33(33,"请求超时"),_34(34,"ESAM的P1P2不正确"),_35(35,"ESAM的LC错误")
    ,_36(36,"比对失败"),_255(255,"其它");

    private final int sign;
    private final String desc;
    Error(int sign, String desc){
        this.sign = sign;
        this.desc = desc;
    }

    public static Error getErrorBySign(byte sign){
        Error[] values = Error.values();
        for(Error error:values) {
            if (error.getSign() == sign) {
                return error;
            }
        }
        return null;
    }
}

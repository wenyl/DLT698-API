package cn.com.wenyl.bs.dlt698.common.constants;

import lombok.Getter;

/**
 * 连接请求类型
 */
@Getter
public enum LinkRequestType {
    LOGIN((byte)0x01,1,"登录"),
    HEARTBEAT((byte)0x02,2,"心跳"),
    LOGOUT((byte)0x03,2,"退出"),
    UNKNOWN(null,-1,"未知");
    private final Byte sign;
    private final int signNum;
    private final String desc;
    LinkRequestType(Byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }

    public static LinkRequestType getLinkRequestTypeBySign(byte sign){
        LinkRequestType[] values = LinkRequestType.values();
        for(LinkRequestType linkRequestType:values) {
            if (linkRequestType.getSign() == sign) {
                return linkRequestType;
            }
        }
        return UNKNOWN;
    }
}

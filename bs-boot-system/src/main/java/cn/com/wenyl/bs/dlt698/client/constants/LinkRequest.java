package cn.com.wenyl.bs.dlt698.client.constants;

import lombok.Getter;

/**
 * 链接请求
 */
@Getter
public enum LinkRequest {
    LOGIN((byte)0x00,0,"登录"),
    HEARTBEAT((byte)0x01,1,"心跳"),
    LOGOUT((byte)0x02,2,"退出登录"),;
    private final byte sign;
    private final int signNum;
    private final String desc;
    LinkRequest(byte sign, int signNum, String desc){
        this.sign = sign;
        this.signNum = signNum;
        this.desc = desc;
    }
    public static LinkRequest getLinkRequestBySign(byte sign){
        LinkRequest[] values = LinkRequest.values();
        for(LinkRequest linkRequest:values) {
            if (linkRequest.getSign() == sign) {
                return linkRequest;
            }
        }
        return null;
    }
}

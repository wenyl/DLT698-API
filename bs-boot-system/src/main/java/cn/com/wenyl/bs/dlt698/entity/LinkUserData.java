package cn.com.wenyl.bs.dlt698.entity;

import lombok.Data;

/**
 * 链路用户数据基本信息，不包含实际数据，实际数据在子类实现
 */
@Data
public class LinkUserData {
    byte PIID;
    byte timeTag;
    byte apdu;
    int length = 0;
    public void plusLength(){
        length++;
    }
    public void plusLength(int length){
        this.length += length;
    }
}

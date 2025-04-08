package cn.com.wenyl.bs.dlt698.common.entity;

import cn.com.wenyl.bs.dlt698.common.constants.ClientAPDU;
import cn.com.wenyl.bs.dlt698.common.entity.LinkUserData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ConnectRequest的数据部分，存放了链接信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConnectRequestData extends LinkUserData {
    private static final byte securityType = (byte)0x00;
    public ConnectRequestData(byte piid, byte timeTag) {
        this.setApdu(ClientAPDU.CONNECT_REQUEST.getSign());
        this.plusLength();
        this.setPIID(piid);
        this.plusLength();
        this.setTimeTag(timeTag);
        this.plusLength();
    }
}

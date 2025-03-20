package cn.com.wenyl.bs.dlt698.entity;

import cn.com.wenyl.bs.dlt698.constants.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GetRequestNormal类型帧的数据部分
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetRequestNormalData extends LinkUserData{
    private byte opera;
    private byte[] oad;

    public GetRequestNormalData( byte piid, OI oi, AttrNum attrNum,byte attributeIndex,byte timeTag) {
        this.setApdu(ClientAPDU.GET_REQUEST.getSign());
        this.plusLength();
        this.setOpera(GetRequest.GET_REQUEST_NORMAL.getSign());
        this.plusLength();
        this.setPIID(piid);
        this.plusLength();
        byte[] oad = new byte[4];
        oad[0] = oi.getSign()[0];
        oad[1] = oi.getSign()[1];
        oad[2] = attrNum.getSign();
        oad[3] = attributeIndex;
        this.setOad(oad);
        this.plusLength(oad.length);
        this.setTimeTag(timeTag);
        this.plusLength();
    }
}

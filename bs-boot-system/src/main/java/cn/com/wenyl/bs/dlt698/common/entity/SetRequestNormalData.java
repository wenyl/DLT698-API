package cn.com.wenyl.bs.dlt698.common.entity;

import cn.com.wenyl.bs.dlt698.common.constants.AttrNum;
import cn.com.wenyl.bs.dlt698.common.constants.ClientAPDU;
import cn.com.wenyl.bs.dlt698.common.constants.OI;
import cn.com.wenyl.bs.dlt698.common.constants.SetRequest;

import cn.com.wenyl.bs.dlt698.common.entity.LinkUserData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SetRequestNormalData类型帧的数据部分
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SetRequestNormalData extends LinkUserData {
    private byte[] dataItem;
    private byte[] oad;
    private byte opera;
    public SetRequestNormalData(byte[] dataItem, byte piid, OI oi, AttrNum attrNum, byte attributeIndex, byte timeTag) {
        this.setApdu(ClientAPDU.SET_REQUEST.getSign());
        this.plusLength();
        this.setOpera(SetRequest.SET_REQUEST_NORMAL.getSign());
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
        this.setDataItem(dataItem);
        this.plusLength(dataItem.length);
        this.setTimeTag(timeTag);
        this.plusLength();
    }
}

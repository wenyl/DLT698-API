package cn.com.wenyl.bs.dlt698.common.entity;

import cn.com.wenyl.bs.dlt698.common.constants.AttrNum;
import cn.com.wenyl.bs.dlt698.common.constants.ClientAPDU;
import cn.com.wenyl.bs.dlt698.common.constants.OI;
import cn.com.wenyl.bs.dlt698.common.constants.ProxyRequestConst;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.ByteBuffer;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProxyTransCommandRequestData extends LinkUserData{
    private byte opera;
    private byte[] oad;
    private byte[] comDcb;
    private byte[] usRevTimeout;
    private byte[] usByteTimeout;
    private byte[] aucCmd;
    public ProxyTransCommandRequestData(byte piid, OI oi, AttrNum attrNum, byte attributeIndex,byte[] aucCmd,byte timeTag){
        this.setApdu(ClientAPDU.PROXY_REQUEST.getSign());
        this.plusLength();
        this.setOpera(ProxyRequest.PROXY_TRANS_COMMAND_REQUEST.getSign());
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
        this.setComDcb(ProxyRequestConst.COM_DCB);
        this.plusLength(ProxyRequestConst.COM_DCB.length);
        this.setUsRevTimeout(ProxyRequestConst.USER_REV_TIMEOUT);
        this.plusLength(ProxyRequestConst.USER_REV_TIMEOUT.length);
        this.setUsByteTimeout(ProxyRequestConst.USER_BYTE_TIMEOUT);
        this.plusLength(ProxyRequestConst.USER_BYTE_TIMEOUT.length);

        System.out.println();
        System.out.println(HexUtils.bytesToHex(aucCmd));
        System.out.println();

        int aucCmdLength = aucCmd.length;
        byte dataLengthByte = (byte)(aucCmdLength & 0xFF);
        byte[] finalAucCmd = new byte[1+aucCmdLength];
        finalAucCmd[0] = dataLengthByte;
        System.arraycopy(aucCmd,0,finalAucCmd,1,aucCmdLength);
        this.setAucCmd(finalAucCmd);
        this.plusLength(finalAucCmd.length);
        this.setTimeTag(timeTag);
        this.plusLength();

    }

}

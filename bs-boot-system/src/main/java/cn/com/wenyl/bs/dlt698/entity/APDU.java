package cn.com.wenyl.bs.dlt698.entity;

import ch.qos.logback.core.net.server.Client;
import cn.com.wenyl.bs.dlt698.constants.ClientAPDU;
import cn.com.wenyl.bs.dlt698.constants.ServerAPDU;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class APDU {
    private byte apdu;
    private byte request;
    private ClientAPDU clientAPDU;
    private byte response;
    private ServerAPDU serverAPDU;
    private byte[] responseData;
    // PIID(1字节)
    private byte piid;
    // 数据(可变长度)
    private byte[] oad;

    private byte timeTag;
    private int byteLength;
    public APDU() {
        this.byteLength = 0;
    }
    public void plusByteLength(){
        this.byteLength++;
    }
    public void plusByteLength(int length){
        this.byteLength += length;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        ClientAPDU clientAPDU = ClientAPDU.getClientAPDUBySign(apdu);
        if(clientAPDU != null){
            sb.append("请求类型--").append(HexUtils.byteToHex(apdu)).append("--").append(clientAPDU.getDesc()).append("\n");
        }
        ServerAPDU serverAPDU = ServerAPDU.getServerAPDUBySign(apdu);
        if(serverAPDU != null){
            sb.append("响应类型--").append(HexUtils.byteToHex(apdu)).append("--").append(serverAPDU.getDesc()).append("\n");
            sb.append("响应数据--").append(HexUtils.bytesToHex(responseData)).append("\n");
        }
        if(piid != 0){
            sb.append("PIID--").append(HexUtils.byteToHex(piid)).append("\n");
        }
        if(oad.length != 0){
            sb.append("oad--").append(HexUtils.bytesToHex(oad)).append("\n");
        }
        if(timeTag != 0){
            sb.append("timeTag--").append(HexUtils.byteToHex(timeTag)).append("\n");

        }
        return sb.toString();
    }
}

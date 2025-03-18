package cn.com.wenyl.bs.dlt698.entity;

import cn.com.wenyl.bs.dlt698.utils.BCDUtils;
import cn.com.wenyl.bs.dlt698.utils.HexUtils;
import lombok.Data;

@Data
public class CSInfo {
    private int funCode;    // 功能码
    private int sc;         // 安全控制
    private int frameFlg;  // 分帧标志
    private int prm;        // 启动标志
    private int dir;        // 方向

    private int aType;    // 地址类型
    private int la;       // 逻辑地址
    private byte[] sa;      // 服务器地址
    private byte ca;        // 客户机地址
    private boolean hasLogicAddress;// 是否有逻辑地址
    private int addressLength;
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("功能码--").append(funCode)
            .append("\n安全控制标识--").append(sc)
            .append("\n分帧标识--").append(frameFlg)
            .append("\n启动标志--").append(prm)
            .append("\n传输方向--").append(dir)
            .append("\n地址类型--").append(aType)
            .append("\n地址长度").append(addressLength);
        if(hasLogicAddress){
            sb.append("\n逻辑地址--").append(la);
        }
        sb.append("\n服务器地址--").append(BCDUtils.decodeBCD(sa))
            .append("\n客户机地址--").append(HexUtils.byteToHex(ca));
        return sb.toString();
    }
}

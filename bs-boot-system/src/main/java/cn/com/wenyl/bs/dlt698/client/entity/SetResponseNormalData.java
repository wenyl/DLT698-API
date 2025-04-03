package cn.com.wenyl.bs.dlt698.client.entity;

import cn.com.wenyl.bs.dlt698.client.constants.DARType;
import cn.com.wenyl.bs.dlt698.client.constants.ServerAPDU;
import cn.com.wenyl.bs.dlt698.client.constants.SetResponse;
import cn.com.wenyl.bs.dlt698.common.LinkUserData;
import cn.com.wenyl.bs.dlt698.common.OAD;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SetResponseNormalData extends LinkUserData {
    // 跟随上报信息域
    private byte followReport;
    // OAD字节数据
    private byte[] oadBytes;
    // OAD字节数据的映射结果
    private OAD oad;
    private DARType darType;
    private ServerAPDU serverAPDU;
    private SetResponse setResponse;
}

package cn.com.wenyl.bs.dlt698.common.entity;


import cn.com.wenyl.bs.dlt698.common.constants.ServerAPDU;
import cn.com.wenyl.bs.dlt698.common.entity.dto.ConnectInfoDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ConnectResponse类型帧的数据部分
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConnectResponseData extends LinkUserData {
    private ConnectInfoDto connectInfo;
    // 厂商代码
    private byte[] oemId;
    // 软件版本号
    private byte[] softwareVersion;
    // 软件版本日期
    private byte[] softwareDate;
    // 软件版本号
    private byte[] hardwareVersion;
    // 硬件版本日期
    private byte[] hardwareDate;
    // 厂商扩展信息
    private byte[] oemExtend;
    // 响应类型
    private ServerAPDU serverAPDU;

    // 应用链接请求认证的结果，和securityData实际上698协议的ConnectResponseInfo对象，这里不做封装了
    private byte[] connectResult;
    // 认证附加信息
    private byte[] securityData;
    // 跟随上报信息域
    private byte[] followReport;

}

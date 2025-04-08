package cn.com.wenyl.bs.dlt698.common.entity;

import cn.com.wenyl.bs.dlt698.common.constants.DataType;
import cn.com.wenyl.bs.dlt698.common.constants.GetResponse;
import cn.com.wenyl.bs.dlt698.common.constants.GetResultType;
import cn.com.wenyl.bs.dlt698.common.constants.ServerAPDU;

import cn.com.wenyl.bs.dlt698.common.entity.LinkUserData;
import cn.com.wenyl.bs.dlt698.common.entity.OAD;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GetResponseNormal类型帧的数据部分
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetResponseNormalData extends LinkUserData {
    // 跟随上报信息域
    private byte[] followReport;
    // OAD字节数据
    private byte[] oadBytes;
    // OAD字节数据的映射结果
    private OAD oad;
    // 返回值的数据类型
    private DataType dataType;
    // 返回值的字节长度
    private int length;
    // 实际的数据部分
    private byte[] data;
    // 响应类型
    private ServerAPDU serverAPDU;
    // 操作的映射值
    private GetResponse getResponse;
    // 结果类型的映射值
    private GetResultType getResultType;
}

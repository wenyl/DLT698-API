package cn.com.wenyl.bs.dlt698.entity;

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
}

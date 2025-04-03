package cn.com.wenyl.bs.dlt698.client.entity;

import cn.com.wenyl.bs.dlt698.common.Frame;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GetResponseNormal类型帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetResponseNormalFrame extends Frame {
    private GetResponseNormalData normalData;

}

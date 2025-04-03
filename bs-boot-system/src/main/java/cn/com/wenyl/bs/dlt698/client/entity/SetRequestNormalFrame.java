package cn.com.wenyl.bs.dlt698.client.entity;

import cn.com.wenyl.bs.dlt698.common.Frame;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SetRequestNormal类型帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SetRequestNormalFrame extends Frame {
    private SetRequestNormalData data;
}

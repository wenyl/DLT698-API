package cn.com.wenyl.bs.dlt698.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SetRequestNormal类型帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SetRequestNormalFrame extends Frame{
    private SetRequestNormalData data;
}

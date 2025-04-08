package cn.com.wenyl.bs.dlt698.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GetRequestNormal类型帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetRequestNormalFrame extends Frame {
    private GetRequestNormalData data;
}

package cn.com.wenyl.bs.dlt698.common.entity;

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

package cn.com.wenyl.bs.dlt698.net4g.entity;

import cn.com.wenyl.bs.dlt698.common.entity.Frame;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * LinkResponse类型帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LinkRequestFrame extends Frame {
    private cn.com.wenyl.bs.dlt698.net4g.entity.LinkRequestData linkRequestData;
}

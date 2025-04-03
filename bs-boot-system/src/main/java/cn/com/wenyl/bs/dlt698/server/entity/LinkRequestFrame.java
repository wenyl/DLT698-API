package cn.com.wenyl.bs.dlt698.server.entity;

import cn.com.wenyl.bs.dlt698.common.Frame;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * LinkResponse类型帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LinkRequestFrame extends Frame {
    private LinkRequestData linkRequestData;
}

package cn.com.wenyl.bs.dlt698.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ConnectRequest类型的帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConnectRequestFrame extends Frame {
    private ConnectRequestData connectRequestData;

}

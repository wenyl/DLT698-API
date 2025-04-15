package cn.com.wenyl.bs.dlt698.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ProxyTransCommandRequest --请求代理透明转发命令帧
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProxyTransCommandRequestFrame extends Frame{
    private ProxyTransCommandRequestData data;
}

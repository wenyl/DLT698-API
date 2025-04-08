package cn.com.wenyl.bs.dlt698.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LinkResponseFrame extends Frame{
    private LinkResponseData linkUserData;
}

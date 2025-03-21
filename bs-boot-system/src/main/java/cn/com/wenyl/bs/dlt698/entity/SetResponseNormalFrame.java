package cn.com.wenyl.bs.dlt698.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SetResponseNormalFrame  extends Frame{
    private SetResponseNormalData data;
}

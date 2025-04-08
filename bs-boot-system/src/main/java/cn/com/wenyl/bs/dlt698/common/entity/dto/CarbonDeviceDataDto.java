package cn.com.wenyl.bs.dlt698.common.entity.dto;

import lombok.Data;

@Data
public class CarbonDeviceDataDto {
    // 电流数组或单个值
    private Object electricCurrent;
    // 电压数组或单个值
    private Object voltage;
    // 正向碳排放
    private Object fce;
    // 反向碳排放
    private Object rce;
    // 正向有功电能量
    private Object paee;
    // 反向有功电能量
    private Object raee;
}

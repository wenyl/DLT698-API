package cn.com.wenyl.bs.dlt698.common.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarbonDeviceTaskMsgDto {
    // 任务ID
    private long id;
    // 任务结束时间
    private LocalDateTime endTime;
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
    // 碳因子
    private double carbonFactor = 0.1235;
}

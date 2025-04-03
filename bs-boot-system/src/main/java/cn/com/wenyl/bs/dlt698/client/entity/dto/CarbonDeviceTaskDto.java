package cn.com.wenyl.bs.dlt698.client.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarbonDeviceTaskDto {
    // id
    private int jobId;
    // 任务结束时间
    private LocalDateTime endTime;
    // 任务返回结果
    private String messages;
}

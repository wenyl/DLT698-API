package cn.com.wenyl.bs.dlt698.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class LinkResponseData extends LinkUserData{
    private byte result;
    private byte[] requestTimeBytes;
    private LocalDateTime requestTime;
    private byte[] receiveTimeBytes;
    private LocalDateTime receiveTime;
    private byte[] responseTimeBytes;
    private LocalDateTime responseTime;
}

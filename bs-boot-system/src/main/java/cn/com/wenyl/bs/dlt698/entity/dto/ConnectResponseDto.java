package cn.com.wenyl.bs.dlt698.entity.dto;

import lombok.Data;

@Data
public class ConnectResponseDto {
    private String oemID;
    private String softwareVersion;
    private String softwareDate;
    private String hardwareVersion;
    private String hardwareDate;
    public String getOemExtend;
}

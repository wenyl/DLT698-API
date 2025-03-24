package cn.com.wenyl.bs.dlt698.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "设置碳因子",description = "给指定设备设置电碳因子")
public class CarbonFactorDto {
    @ApiModelProperty(value = "碳表地址")
    private String carbonDeviceAddress;
    @ApiModelProperty(value = "电碳因子值数组")
    private double[] carbonFactor;
}

package cn.com.wenyl.bs.dlt698.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 碳表任务
 * </p>
 *
 * @author baomidou
 * @since 2025-03-26
 */
@Getter
@Setter
@ToString
@TableName("carbon_device_task")
@ApiModel(value = "CarbonDeviceTask对象", description = "碳表任务")
public class CarbonDeviceTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String jobName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    /**
     * 是否大屏数据，默认0
     */
    @ApiModelProperty("是否大屏数据，默认0")
    private boolean screenData;

    /**
     * 碳表地址
     */
    @ApiModelProperty("碳表地址")
    private String carbonDeviceAddress;
}

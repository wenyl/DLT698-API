package cn.com.wenyl.bs.dlt698.net4g.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备历史正向有功值
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("forward_active_power")
@ApiModel(value="ForwardActivePower对象", description="正向有功")
public class ForwardActivePower implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    @TableField("device_id")
    private Integer deviceId;

    @ApiModelProperty(value = "消息ID(device_msg_his表的ID)")
    @TableField("msg_id")
    private Integer msgId;

    @ApiModelProperty(value = "当前值")
    private Double now;

    @ApiModelProperty(value = "尖时段值")
    private Double sharp;

    @ApiModelProperty(value = "峰时段值")
    private Double peak;

    @ApiModelProperty(value = "平时段值")
    private Double shoulder;

    @ApiModelProperty(value = "谷时段值")
    @TableField("off_peak")
    private Double offPeak;

    @ApiModelProperty(value = "时间")
    @TableField("date_time")
    private LocalDateTime dateTime;


}

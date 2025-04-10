package cn.com.wenyl.bs.dlt698.net4g.entity;

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
 * 设备电压历史值
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Voltage对象", description="设备电压历史值")
public class Voltage implements Serializable {

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

    @ApiModelProperty(value = "A相电压")
    @TableField("voltage_a")
    private Double voltageA;

    @ApiModelProperty(value = "B相电压")
    @TableField("voltage_b")
    private Double voltageB;

    @ApiModelProperty(value = "C相电压")
    @TableField("voltage_c")
    private Double voltageC;

    @ApiModelProperty(value = "时间")
    @TableField("date_time")
    private LocalDateTime dateTime;


}

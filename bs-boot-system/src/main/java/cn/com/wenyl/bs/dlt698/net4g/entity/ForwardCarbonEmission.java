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
 * 正向碳排放
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("forward_carbon_emission")
@ApiModel(value="ForwardCarbonEmission对象", description="正向碳排放")
public class ForwardCarbonEmission implements Serializable {

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

    @ApiModelProperty(value = "碳排放因子")
    @TableField("carbon_factor")
    private Double carbonFactor;

    @ApiModelProperty(value = "碳排放量")
    @TableField("carbon_emission")
    private Integer carbonEmission;

    @ApiModelProperty(value = "时间")
    @TableField("date_time")
    private LocalDateTime dateTime;


}

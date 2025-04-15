package cn.com.wenyl.bs.dlt698.net4g.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 碳表信息
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("carbon_device")
@ApiModel(value="CarbonDevice对象", description="碳表信息")
public class CarbonDevice implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "设备ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备IP")
    @TableField("device_ip")
    private String deviceIp;


    @ApiModelProperty(value = "设备在状态1为在线,0为离线")
    @TableField("device_status")
    private int deviceStatus = 0;

    @ApiModelProperty(value = "碳表地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}

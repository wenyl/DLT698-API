package cn.com.wenyl.bs.dlt698.net4g.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备消息历史关系
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("station_device_msg_rela")
@ApiModel(value="StationDeviceMsgRela对象", description="设备消息历史关系")
public class StationDeviceMsgRela implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "唯一标识符")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    @TableField("device_id")
    private Integer deviceId;

    @ApiModelProperty(value = "发起方 0表示由主站发起碳表回复，1表示由碳表发起主站回复")
    private Integer dir;

    @ApiModelProperty(value = "主站发送的数据ID")
    @TableField("main_station_msg_id")
    private Integer mainStationMsgId;

    @ApiModelProperty(value = "碳表发送的数据ID")
    @TableField("device_msg_id")
    private Integer deviceMsgId;


}

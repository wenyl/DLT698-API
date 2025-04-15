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
 * 设备错误消息历史
 * </p>
 *
 * @author ${author}
 * @since 2025-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_error_msg_his")
@ApiModel(value="DeviceErrorMsgHis对象", description="设备错误消息历史")
public class DeviceErrorMsgHis implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    @TableField("device_id")
    private Integer deviceId;

    @ApiModelProperty(value = "字节数据")
    @TableField("byte_data")
    private String byteData;

    @ApiModelProperty(value = "数据长度")
    @TableField("data_length")
    private Integer dataLength;

    @ApiModelProperty(value = "错误消息")
    @TableField("error_msg")
    private String errorMsg;

    @ApiModelProperty(value = "接收时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}

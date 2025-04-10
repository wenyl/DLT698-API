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
 * 设备消息历史
 * </p>
 *
 * @author ${author}
 * @since 2025-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_msg_his")
@ApiModel(value="DeviceMsgHis对象", description="设备消息历史")
public class DeviceMsgHis implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "唯一标识符")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备ID")
    @TableField("device_id")
    private Integer deviceId;

    @ApiModelProperty(value = "传输方向")
    private Integer dir;

    @ApiModelProperty(value = "启动标志")
    private Integer prm;

    @ApiModelProperty(value = "请求类型")
    private String apdu;

    @ApiModelProperty(value = "请求操作")
    @TableField("apdu_opera")
    private String apduOpera;

    @ApiModelProperty(value = "OI接口编号")
    private String oi;

    @ApiModelProperty(value = "属性编号")
    @TableField("attr_num")
    private String attrNum;

    @ApiModelProperty(value = "属性特征索引")
    @TableField("attr_index")
    private String attrIndex;

    @ApiModelProperty(value = "原始字节数据")
    @TableField("byte_data")
    private String byteData;

    @ApiModelProperty(value = "数据长度(字节)")
    @TableField("data_length")
    private int dataLength;

    @ApiModelProperty(value = "时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}

package cn.com.wenyl.bs.dlt698.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
/**
 * <p>
 * 碳表任务报文
 * </p>
 *
 * @author baomidou
 * @since 2025-03-26
 */
@Getter
@Setter
@ToString
@TableName("carbon_device_message")
@ApiModel(value = "CarbonDeviceMessage对象", description = "碳表任务报文")
public class CarbonDeviceMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 碳表任务ID
     */
    @ApiModelProperty("碳表任务ID")
    private Long jobId;

    /**
     * 操作名
     */
    @ApiModelProperty("操作名")
    private String operateName;

    /**
     * 发送的字节数据
     */
    @ApiModelProperty("发送的字节数据")
    private String sendByteStr;

    /**
     * 接收的字节数据
     */
    @ApiModelProperty("接收的字节数据")
    private String receiveByteStr;

    /**
     * 解析的数据值(json形式)
     */
    @ApiModelProperty("解析的数据值(json形式)")
    private String valueJson;

    /**
     * 值的标签
     */
    @ApiModelProperty("值的标签")
    private String valueLabel;

    /**
     * 值的标识
     */
    @ApiModelProperty("值的标识")
    private String valueSign;
}

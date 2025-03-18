package cn.com.wenyl.bs.system.sysDept.entity;

import java.lang.String;
import java.lang.Short;
import java.lang.Integer;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 组织机构
 */
@Data
@TableName("sys_dept")
@ApiModel(value="SysDept对象", description="组织机构")
public class SysDept implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 上级ID
     */
    @ApiModelProperty(value = "上级ID")
    private String parentId;

    /**
     * 是否有下级，0-否，1-是
     */
    @ApiModelProperty(value = "是否有下级，0-否，1-是")
    private Short isLeaf;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer deptOrder;

    /**
     * 描述信息
     */
    @ApiModelProperty(value = "描述信息")
    private String deptDesc;

    /**
     * 机构类别 1公司，2部门，3项目组
     */
    @ApiModelProperty(value = "机构类别 1公司，2部门，3项目组")
    private Short deptCategory;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 删除状态（0-正常，1-已删除）
     */
    @ApiModelProperty(value = "删除状态（0-正常，1-已删除）")
    private Short delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
}

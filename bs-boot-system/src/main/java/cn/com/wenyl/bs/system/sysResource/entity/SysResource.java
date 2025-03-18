package cn.com.wenyl.bs.system.sysResource.entity;

import java.lang.String;
import java.lang.Integer;
import java.lang.Double;
import java.lang.Short;
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
 * 资源权限表
 */
@Data
@TableName("sys_resource")
@ApiModel(value="SysResource对象", description="资源权限表")
public class SysResource implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 父资源id
     */
    @ApiModelProperty(value = "父资源id")
    private String parentId;

    /**
     * 资源名称(菜单名、按钮名)
     */
    @ApiModelProperty(value = "资源名称(菜单名、按钮名)")
    private String resourceName;

    /**
     * vue菜单访问路径
     */
    @ApiModelProperty(value = "vue菜单访问路径")
    private String menuUrl;

    /**
     * 菜单路径(包含父菜单地址)
     */
    @ApiModelProperty(value = "vue菜单访问路径")
    private String menuPath;

    /**
     * vue菜单组件
     */
    @ApiModelProperty(value = "vue菜单组件")
    private String menuComponent;

    /**
     * 一级菜单跳转地址
     */
    @ApiModelProperty(value = "一级菜单跳转地址")
    private String redirect;

    /**
     * 资源类型(0:菜单; 1:按钮)
     */
    @ApiModelProperty(value = "资源类型(0:菜单; 1:按钮;)")
    private Integer resourceType;

    /**
     * 资源权限编码
     */
    @ApiModelProperty(value = "资源权限编码")
    private String perms;

    /**
     * 资源排序
     */
    @ApiModelProperty(value = "资源排序")
    private Double sortNo;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String menuIcon;

    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    @ApiModelProperty(value = "是否路由菜单: 0:不是  1:是（默认值1）")
    private Short isRoute;

    /**
     * 是否叶子节点:    1:是   0:不是
     */
    @ApiModelProperty(value = "是否叶子节点:    1:是   0:不是")
    private Short isLeaf;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 删除状态 0正常 1已删除
     */
    @ApiModelProperty(value = "删除状态 0正常 1已删除")
    private Integer delFlag;
}

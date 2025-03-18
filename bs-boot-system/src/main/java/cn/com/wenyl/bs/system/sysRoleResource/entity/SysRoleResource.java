package cn.com.wenyl.bs.system.sysRoleResource.entity;

import java.lang.String;
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
 * 角色权限表
 */
@Data
@TableName("sys_role_resource")
@ApiModel(value="SysRoleResource对象", description="角色权限表")
public class SysRoleResource implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "")
    private String id;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private String roleId;

    /**
     * 系统资源ID
     */
    @ApiModelProperty(value = "系统资源ID")
    private String sysResourceId;

    /**
     * 资源在树中是否半选中
     */
    @ApiModelProperty(value = "是否半选中")
    private Boolean isHalfChecked;

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
}

package cn.com.wenyl.bs.system.sysUser.vo;

import cn.com.wenyl.bs.system.sysRole.entity.SysRole;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2024年03月12日 13:45
 */
@Data
public class SysUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String realname;
    private String password;
    private String avatar;
    private String email;
    private String phone;
    private String deptId;
    private Short status;
    private Short delFlag;
    private String duties;
    private String roles;
    private String roleNames;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private List<String> roleIdList;
}

package cn.com.wenyl.bs.system.auth.vo;

import lombok.Data;

/**
 * @author Swimming Dragon
 * @description: 登录后返回前端的角色信息
 * @date 2023年12月25日 14:26
 */
@Data
public class SysRoleVo {
    private String roleName;
    private String roleCode;
}

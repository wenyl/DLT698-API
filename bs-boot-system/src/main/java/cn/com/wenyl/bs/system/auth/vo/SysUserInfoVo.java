package cn.com.wenyl.bs.system.auth.vo;

import cn.com.wenyl.bs.system.sysResource.entity.SysResource;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author Swimming Dragon
 * @description: 登录后获取的用户信息
 * @date 2023年12月25日 14:07
 */
@Data
public class SysUserInfoVo {
    private String userId;
    private String username;
    private String realName;
    private String avatar;
    private String email;
    private String phone;
    private String deptId;
    private String deptName;
    // 用户拥有的角色
    private Set<String> roles;
}

package cn.com.wenyl.bs.system.sysUserRole.service;

import cn.com.wenyl.bs.system.sysUserRole.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysUserRoleService extends IService<SysUserRole> {

    void updateUserRoles(String userId, List<String> roleIdList);
    void saveUserRoles(String userId, List<String> roleIdList);

    void removeByUserId(String userId);

    void removeByUserIds(List<String> idList);
}
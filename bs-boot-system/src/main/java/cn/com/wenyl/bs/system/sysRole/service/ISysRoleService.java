package cn.com.wenyl.bs.system.sysRole.service;

import cn.com.wenyl.bs.system.sysRole.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysRoleService extends IService<SysRole> {
    List<SysRole> getUserRoleList(String userId);

}
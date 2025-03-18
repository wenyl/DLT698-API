package cn.com.wenyl.bs.system.sysUser.service;

import cn.com.wenyl.bs.system.sysUser.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysUserService extends IService<SysUser> {
    SysUser getByUserName(String username);

}
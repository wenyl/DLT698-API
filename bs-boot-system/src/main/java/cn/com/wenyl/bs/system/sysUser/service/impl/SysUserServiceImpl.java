package cn.com.wenyl.bs.system.sysUser.service.impl;

import cn.com.wenyl.bs.system.sysRole.entity.SysRole;
import cn.com.wenyl.bs.system.sysRole.mapper.SysRoleMapper;
import cn.com.wenyl.bs.system.sysRole.service.ISysRoleService;
import cn.com.wenyl.bs.system.sysUser.entity.SysUser;
import cn.com.wenyl.bs.system.sysUser.mapper.SysUserMapper;
import cn.com.wenyl.bs.system.sysUser.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service(value = "sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Resource(name = "sysRoleService")
    private ISysRoleService sysRoleService;

    @Override
    public SysUser getByUserName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        queryWrapper.eq(SysUser::getDelFlag,0);
        return getOne(queryWrapper);
    }



}

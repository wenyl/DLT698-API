package cn.com.wenyl.bs.system.sysRole.service.impl;

import cn.com.wenyl.bs.system.sysRole.entity.SysRole;
import cn.com.wenyl.bs.system.sysRole.mapper.SysRoleMapper;
import cn.com.wenyl.bs.system.sysRole.service.ISysRoleService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;

@Service(value = "sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Override
    public List<SysRole> getUserRoleList(String userId) {
        return this.getBaseMapper().getUserRoleList(userId);
    }
}

package cn.com.wenyl.bs.system.sysUserRole.service.impl;

import cn.com.wenyl.bs.system.sysUserRole.entity.SysUserRole;
import cn.com.wenyl.bs.system.sysUserRole.mapper.SysUserRoleMapper;
import cn.com.wenyl.bs.system.sysUserRole.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRoles(String userId, List<String> roleIdList) {
        LambdaUpdateWrapper<SysUserRole> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(SysUserRole::getUserId,userId);
        deleteWrapper.in(SysUserRole::getRoleId,roleIdList);
        this.remove(deleteWrapper);

        this.saveUserRoles(userId,roleIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoles(String userId, List<String> roleIdList) {
        List<SysUserRole> list = new ArrayList<>();
        for(String roleId:roleIdList){
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(userId);
            list.add(sysUserRole);
        }

        if(list.size() >0){
            this.saveBatch(list);
        }
    }

    @Override
    public void removeByUserId(String userId) {
        LambdaUpdateWrapper<SysUserRole> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(SysUserRole::getUserId,userId);
        this.remove(deleteWrapper);
    }

    @Override
    public void removeByUserIds(List<String> idList) {
        LambdaUpdateWrapper<SysUserRole> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.in(SysUserRole::getUserId,idList);
        this.remove(deleteWrapper);
    }
}

package cn.com.wenyl.bs.system.sysRoleResource.service.impl;

import cn.com.wenyl.bs.system.sysResource.entity.SysResource;
import cn.com.wenyl.bs.system.sysResource.vo.SysResourceSelectTreeVo;
import cn.com.wenyl.bs.system.sysResource.vo.SysResourceTableTreeVo;
import cn.com.wenyl.bs.system.sysRoleResource.dto.RoleResourcesDto;
import cn.com.wenyl.bs.system.sysRoleResource.entity.SysRoleResource;
import cn.com.wenyl.bs.system.sysRoleResource.mapper.SysRoleResourceMapper;
import cn.com.wenyl.bs.system.sysRoleResource.service.ISysRoleResourceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service(value = "sysRoleResourceService")
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements ISysRoleResourceService {
    
    @Override
    public List<SysRoleResource> getRoleResourceIds(String roleId) {
        // 查询该角色的ID
        LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleResource::getRoleId,roleId);
        return this.getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public void removeByRoleId(String roleId) {
        LambdaUpdateWrapper<SysRoleResource> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysRoleResource::getRoleId,roleId);
        this.remove(updateWrapper);
    }

    @Override
    public void removeByRoleIds(List<String> roleIds) {
        LambdaUpdateWrapper<SysRoleResource> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysRoleResource::getRoleId,roleIds);
        this.remove(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleResources(RoleResourcesDto roleResourcesDto) {
        String roleId = roleResourcesDto.getRoleId();
        this.removeByRoleId(roleId);

        List<SysRoleResource> roleResources = new ArrayList<>();
        for(String resourceId: roleResourcesDto.getCheckedKeys()){
            SysRoleResource sysRoleResource = new SysRoleResource();
            sysRoleResource.setRoleId(roleId);
            sysRoleResource.setSysResourceId(resourceId);
            sysRoleResource.setIsHalfChecked(false);
            roleResources.add(sysRoleResource);
        }
        for(String resourceId: roleResourcesDto.getHalfCheckedKeys()){
            SysRoleResource sysRoleResource = new SysRoleResource();
            sysRoleResource.setRoleId(roleId);
            sysRoleResource.setSysResourceId(resourceId);
            sysRoleResource.setIsHalfChecked(true);
            roleResources.add(sysRoleResource);
        }
        if(roleResources.size() > 0){
            this.saveBatch(roleResources);
        }
    }
}

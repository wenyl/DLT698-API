package cn.com.wenyl.bs.system.sysRoleResource.service;

import cn.com.wenyl.bs.system.sysResource.vo.SysResourceSelectTreeVo;
import cn.com.wenyl.bs.system.sysRoleResource.dto.RoleResourcesDto;
import cn.com.wenyl.bs.system.sysRoleResource.entity.SysRoleResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysRoleResourceService extends IService<SysRoleResource> {

    List<SysRoleResource> getRoleResourceIds(String roleId);

    void removeByRoleId(String roleId);

    void removeByRoleIds(List<String> roleIds);

    void updateRoleResources(RoleResourcesDto roleResourcesDto);
}
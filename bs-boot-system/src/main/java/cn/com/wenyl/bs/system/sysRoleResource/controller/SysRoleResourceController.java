package cn.com.wenyl.bs.system.sysRoleResource.controller;

import cn.com.wenyl.bs.system.sysRoleResource.dto.RoleResourcesDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.com.wenyl.bs.system.sysRoleResource.service.ISysRoleResourceService;
import cn.com.wenyl.bs.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/system/sysRoleResource")
@Api(tags="角色权限")
public class SysRoleResourceController{

    @Resource(name = "sysRoleResourceService")
    private ISysRoleResourceService sysRoleResourceService;

    @PostMapping("/updateRoleResources")
    @ApiOperation(value="角色权限-更新角色权限", notes="角色权限-更新角色权限")
    public R<?> updateRoleResources(@RequestBody RoleResourcesDto roleResourcesDto){
        sysRoleResourceService.updateRoleResources(roleResourcesDto);
        return R.ok("操作成功");
    }
}

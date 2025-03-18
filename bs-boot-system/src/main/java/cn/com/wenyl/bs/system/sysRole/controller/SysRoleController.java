package cn.com.wenyl.bs.system.sysRole.controller;

import cn.com.wenyl.bs.system.sysResource.service.ISysResourceService;
import cn.com.wenyl.bs.system.sysResource.vo.SysResourceSelectTreeVo;
import cn.com.wenyl.bs.system.sysResource.vo.SysRoleResourcesSelectTreeVo;
import cn.com.wenyl.bs.system.sysRole.vo.SysRoleSelectVo;
import cn.com.wenyl.bs.system.sysRoleResource.entity.SysRoleResource;
import cn.com.wenyl.bs.system.sysRoleResource.service.ISysRoleResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.com.wenyl.bs.system.sysRole.entity.SysRole;
import cn.com.wenyl.bs.system.sysRole.service.ISysRoleService;
import cn.com.wenyl.bs.utils.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/system/sysRole")
@Api(tags="角色")
public class SysRoleController{

    @Resource(name = "sysRoleService")
    private ISysRoleService sysRoleService;
    @Resource(name = "sysResourceService")
    private ISysResourceService sysResourceService;
    @Resource(name = "sysRoleResourceService")
    private ISysRoleResourceService sysRoleResourceService;

    @PostMapping("/save")
    @ApiOperation(value="角色-保存", notes="角色-保存")
    public R<?> save(@RequestBody SysRole entity){
        sysRoleService.save(entity);
        return R.ok();
    }

    @DeleteMapping(value = "/removeById")
    @ApiOperation(value="角色-根据ID删除", notes="角色-根据ID删除")
    public R<?> removeById(@RequestParam(name="id") String id) {
        sysRoleService.removeById(id);
        sysRoleResourceService.removeByRoleId(id);
        return R.ok("删除成功!");
    }

    @DeleteMapping(value = "/removeByIds")
    @ApiOperation(value="角色-根据ID批量删除", notes="角色-根据ID批量删除")
    public R<?> removeByIds(@RequestParam(name="ids") String ids) {
        List<String> roleIds = Arrays.asList(ids.split(","));
        sysRoleService.removeByIds(roleIds);
        sysRoleResourceService.removeByRoleIds(roleIds);
        return R.ok("批量删除成功!");
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.PUT,RequestMethod.POST})
    @ApiOperation(value="角色-根据ID更新", notes="角色-根据ID更新")
    public R<?> updateById(@RequestBody SysRole entity){
        sysRoleService.updateById(entity);
        return R.ok();
    }

    @GetMapping(value = "/queryById")
    @ApiOperation(value="角色-通过id查询", notes="角色-通过id查询")
    public R<SysRole> queryById(@RequestParam(name="id") String id) {
        SysRole entity = sysRoleService.getById(id);
        if(entity==null) {
            return R.error("未找到对应数据");
        }
        return R.ok(entity);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value="角色-分页查询", notes="角色-分页列表查询")
    public R<IPage<SysRole>> queryPageList(
                                                       SysRole sysRole,
                                                       @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                       HttpServletRequest req) {
        sysRole.setDelFlag(Short.valueOf("0"));
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery(sysRole);
        Page<SysRole> page = new Page<>(currentPage, pageSize);
        IPage<SysRole> pageList = sysRoleService.page(page,queryWrapper);
        return R.ok(pageList);
    }
    @GetMapping(value = "/querySysRoleSelectList")
    @ApiOperation(value="角色-查询下拉框角色", notes="角色-查询下拉框角色")
    public R<List<SysRoleSelectVo>> querySysRoleSelectList(){
        SysRole sysRoleQuery = new SysRole();
        sysRoleQuery.setDelFlag(Short.valueOf("0"));
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery(sysRoleQuery);
        List<SysRole> list = sysRoleService.list(queryWrapper);
        List<SysRoleSelectVo> collect = list.stream().map(sysRole -> {
            SysRoleSelectVo sysRoleSelectVo = new SysRoleSelectVo();
            sysRoleSelectVo.setId(sysRole.getId());
            sysRoleSelectVo.setLabel(sysRole.getRoleName());
            sysRoleSelectVo.setValue(sysRole.getId());
            return sysRoleSelectVo;
        }).collect(Collectors.toList());
        return R.ok(collect);
    }



    @GetMapping(value = "/getSysResourceSelectTree")
    @ApiOperation(value="资源权限表-获取指定角色下拉框资源树", notes="资源权限表-获取指定角色下拉框资源树")
    public R<SysRoleResourcesSelectTreeVo> getSysResourceSelectTree(@RequestParam(value = "roleId") String roleId){
        SysRoleResourcesSelectTreeVo ret = new SysRoleResourcesSelectTreeVo();
        List<SysRoleResource> selectedResources = sysRoleResourceService.getRoleResourceIds(roleId);
        List<SysResourceSelectTreeVo> treeVo = sysResourceService.getSysResourceSelectTree();
        ret.setSelectedResources(selectedResources);
        ret.setSelectTreeVo(treeVo);
        return R.ok(ret);
    }
}

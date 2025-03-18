package cn.com.wenyl.bs.system.sysUser.controller;

import cn.com.wenyl.bs.system.sysRole.entity.SysRole;
import cn.com.wenyl.bs.system.sysRole.service.ISysRoleService;
import cn.com.wenyl.bs.system.sysUser.dto.SysUserDto;
import cn.com.wenyl.bs.system.sysUser.vo.SysUserVo;
import cn.com.wenyl.bs.system.sysUserRole.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.com.wenyl.bs.system.sysUser.entity.SysUser;
import cn.com.wenyl.bs.system.sysUser.service.ISysUserService;
import cn.com.wenyl.bs.utils.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/system/sysUser")
@Api(tags="系统用户")
public class SysUserController{

    @Resource(name = "sysUserService")
    private ISysUserService sysUserService;
    @Resource(name = "sysRoleService")
    private ISysRoleService sysRoleService;

    @Resource(name = "sysUserRoleService")
    private ISysUserRoleService sysUserRoleService;

    @PostMapping("/save")
    @ApiOperation(value="系统用户-保存", notes="系统用户-保存")
    public R<?> save(@RequestBody SysUserDto dto){
        SysUser entity = new SysUser();
        BeanUtils.copyProperties(dto,entity);
        sysUserService.save(entity);
        sysUserRoleService.saveUserRoles(entity.getId(),dto.getRoleIdList());
        return R.ok();
    }

    @DeleteMapping(value = "/removeById")
    @ApiOperation(value="系统用户-根据ID删除", notes="系统用户-根据ID删除")
    public R<?> removeById(@RequestParam(name="id") String id) {
        sysUserService.removeById(id);
        sysUserRoleService.removeByUserId(id);
        return R.ok("删除成功!");
    }

    @DeleteMapping(value = "/removeByIds")
    @ApiOperation(value="系统用户-根据ID批量删除", notes="系统用户-根据ID批量删除")
    public R<?> removeByIds(@RequestParam(name="ids") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        sysUserService.removeByIds(idList);
        sysUserRoleService.removeByUserIds(idList);
        return R.ok("批量删除成功!");
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.PUT,RequestMethod.POST})
    @ApiOperation(value="系统用户-根据ID更新", notes="系统用户-根据ID更新")
    public R<?> updateById(@RequestBody SysUserDto dto){
        SysUser entity = new SysUser();
        BeanUtils.copyProperties(dto,entity);
        sysUserService.updateById(entity);
        sysUserRoleService.updateUserRoles(entity.getId(),dto.getRoleIdList());
        return R.ok();
    }

    @GetMapping(value = "/queryById")
    @ApiOperation(value="系统用户-通过id查询", notes="系统用户-通过id查询")
    public R<SysUserVo> queryById(@RequestParam(name="id") String id) {
        SysUser entity = sysUserService.getById(id);
        if(entity==null) {
            return R.error("未找到对应数据");
        }
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(entity,sysUserVo);
        List<SysRole> userRoleList = sysRoleService.getUserRoleList(entity.getId());
        List<String> idList = userRoleList.stream().map(sysRole -> sysRole.getId()).collect(Collectors.toList());
        sysUserVo.setRoleIdList(idList);
        return R.ok(sysUserVo);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value="系统用户-分页查询", notes="系统用户-分页列表查询")
    public R<IPage<SysUser>> queryPageList(
                                                       SysUser sysUser,
                                                       @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                       HttpServletRequest req) {
        sysUser.setDelFlag(Short.valueOf("0"));
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery(sysUser);
        Page<SysUser> page = new Page<>(currentPage, pageSize);
        IPage<SysUser> pageList = sysUserService.page(page,queryWrapper);
        return R.ok(pageList);
    }
}

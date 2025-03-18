package cn.com.wenyl.bs.system.auth.controller;

import cn.com.wenyl.bs.config.jwt.JwtConstant;
import cn.com.wenyl.bs.config.jwt.JwtUtil;
import cn.com.wenyl.bs.system.auth.dto.LoginDto;
import cn.com.wenyl.bs.system.auth.vo.*;
import cn.com.wenyl.bs.system.sysDept.entity.SysDept;
import cn.com.wenyl.bs.system.sysDept.service.ISysDeptService;
import cn.com.wenyl.bs.system.sysResource.entity.SysResource;
import cn.com.wenyl.bs.system.sysResource.service.ISysResourceService;
import cn.com.wenyl.bs.system.sysRole.service.ISysRoleService;
import cn.com.wenyl.bs.system.sysUser.entity.SysUser;
import cn.com.wenyl.bs.system.sysUser.service.ISysUserService;
import cn.com.wenyl.bs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author Swimming Dragon
 * @description: 登录接口
 * @date 2023年12月05日 13:23
 */
@Slf4j
@RestController
@RequestMapping("/system/login")
@Api(tags="登录")
public class LoginController {
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysResourceService sysResourceService;
    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostMapping("/loginByUsernameAndPassword")
    @ApiOperation(value="登录-跳转到登录页", notes="登录-跳转到登录页")
    public R<LoginVo> login(@RequestBody @NonNull LoginDto loginDto) {
        // 获取用户
        SysUser sysUser = sysUserService.getByUserName(loginDto.getUsername());
        if(sysUser == null){
            return R.error("用户不存在");
        }
        if(!loginDto.getPassword().equals(sysUser.getPassword())){
            return R.error("密码错误，回到登录页");
        }
        // 获取token
        String token;
        try {
            token = JwtUtil.createToken(loginDto.getUsername(), loginDto.getPassword());

        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
        redisTemplate.opsForValue().set(JwtConstant.PREFIX_SHIRO_WEB_TOKEN+loginDto.getUsername(),token, JwtConstant.ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return R.ok(loginVo);
    }

    @GetMapping(value = "/logout")
    @ApiOperation(value="登录-退出登录", notes="登录-退出登录")
    public R<?> logout(HttpServletRequest request) {
        //用户退出逻辑
        String token = request.getHeader(JwtConstant.REQUEST_HEADER_TOKEN);
        if(StringUtils.isEmpty(token)) {
            return R.error("token为空,退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        SysUser sysUser = sysUserService.getByUserName(username);
        if(sysUser!=null) {
            redisTemplate.opsForValue().getOperations().delete(JwtConstant.PREFIX_SHIRO_WEB_TOKEN+username);
            //调用shiro的logout
            SecurityUtils.getSubject().logout();

            log.info(" 用户名:  "+username+",退出成功！ ");
            return R.ok("退出登录成功！");
        }else {
            log.info(" 用户名:  "+username+"退出登录失败，用户不存在!");
            return R.error("退出登录失败，用户不存在!");
        }
    }

    @GetMapping("/getUserInfoByToken")
    @ApiOperation(value="登录-获取用户信息", notes="登录-获取用户信息")
    public R<SysUserInfoVo> getUserInfoByToken(HttpServletRequest request){
        SysUserInfoVo sysUserInfoVo = new SysUserInfoVo();
        String token = request.getHeader(JwtConstant.REQUEST_HEADER_TOKEN);
        if(StringUtils.isBlank(token)){
            return R.error("token为空,获取用户信息异常");
        }
        String username = JwtUtil.getUsername(token);
        // 获取基本信息
        SysUser sysUser = sysUserService.getByUserName(username);
        if(sysUser == null){
            return R.error("用户"+username+"不存在");
        }
        sysUserInfoVo.setUserId(sysUser.getId());
        sysUserInfoVo.setEmail(sysUser.getEmail());
        sysUserInfoVo.setAvatar(sysUser.getAvatar());
        sysUserInfoVo.setPhone(sysUser.getPhone());
        sysUserInfoVo.setUsername(sysUser.getUsername());
        sysUserInfoVo.setRealName(sysUser.getRealname());

        // 获取部门信息
        String deptId = sysUser.getDeptId();
        if(!StringUtils.isBlank(deptId)){
            SysDept sysDept = sysDeptService.getById(deptId);
            if(sysDept != null){
                sysUserInfoVo.setDeptName(sysDept.getDeptName());
                sysUserInfoVo.setDeptId(sysDept.getId());
            }
        }

        //
        return R.ok(sysUserInfoVo);
    }

    @GetMapping("/getUserResourceByToken")
    @ApiOperation(value="登录-获取用户菜单", notes="登录-获取用户菜单")
    public R<SysUserResourceVo> getUserMenuByToken(HttpServletRequest request){
        String token = request.getHeader(JwtConstant.REQUEST_HEADER_TOKEN);
        if(StringUtils.isBlank(token)){
            return R.error("token为空,获取用户信息异常");
        }
        String username = JwtUtil.getUsername(token);
        // 获取基本信息
        SysUser sysUser = sysUserService.getByUserName(username);
        if(sysUser == null){
            return R.error("用户"+username+"不存在");
        }
        // 获取用户菜单
        List<RouterVo> userRouter = sysResourceService.getUserRouter(sysUser.getId());
        // 获取用户按钮
        List<SysResource> userBtn = sysResourceService.getUserBtn(sysUser.getId());
        // 获取侧边菜单
        List<MenuVo> userMenu = sysResourceService.getUserMenu(sysUser.getId());
        SysUserResourceVo ret = new SysUserResourceVo();
        ret.setRouterList(userRouter);
        ret.setBtnList(userBtn);
        ret.setMenuList(userMenu);
        return R.ok(ret);
    }
}

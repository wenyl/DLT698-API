package cn.com.wenyl.bs.config.jwt;

import cn.com.wenyl.bs.system.sysRole.entity.SysRole;
import cn.com.wenyl.bs.system.sysRole.service.ISysRoleService;
import cn.com.wenyl.bs.system.sysUser.entity.SysUser;
import cn.com.wenyl.bs.system.sysUser.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2023年12月06日 14:15
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysRoleService roleService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String id = (String) principals.getPrimaryPrincipal();
        // 获取角色信息
        List<SysRole> roleList = roleService.getUserRoleList(id);
        //添加角色权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for(SysRole role:roleList){
            simpleAuthorizationInfo.addRole(role.getRoleCode());
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得account，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        // 帐号为空
        if (StringUtils.isBlank(username)) {
            log.error("Token中帐号为空");
            throw new AuthenticationException("Token中帐号为空");
        }
        // 查询用户是否存在
        SysUser sysUser = userService.getByUserName(username);
        if (sysUser == null) {
            throw new AuthenticationException("帐号"+username+"不存在");
        }
        // 验证token和refreshToken
        Boolean exists = redisTemplate.hasKey(JwtConstant.PREFIX_SHIRO_WEB_TOKEN + username);

        if (JwtUtil.verifyToken(token,username,sysUser.getPassword()) && exists != null && exists) {
            return new SimpleAuthenticationInfo(sysUser, token, getName());
        }
        throw new AuthenticationException("Token已过期)");
    }

    /**
     * 指定当前realm适用的token类型为JwtToken
     * @param authenticationToken the token being submitted for authentication. 认证的token
     * @return 如果类型为JwtToken则返回true，否则返回false
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }
}

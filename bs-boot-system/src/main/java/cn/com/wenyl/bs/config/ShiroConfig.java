package cn.com.wenyl.bs.config;

import cn.com.wenyl.bs.config.jwt.JwtFilter;
import cn.com.wenyl.bs.config.jwt.JwtRealm;
import cn.com.wenyl.bs.config.shiro.ShiroRedisCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

/**
 * @author Swimming Dragon
 * @description: 配置shiro
 * @date 2023年12月05日 9:55
 */
@Configuration
public class ShiroConfig {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Bean("jwtRealm")
    public JwtRealm getJwtRealm() {
        return new JwtRealm();
    }

    /**
     * 自定义shiro缓存管理，使用redis作为默认缓存
     * @return shiro缓存管理
     */
    @Bean("cacheManager")
    public CacheManager shiroRedisCacheManager() {
        return new ShiroRedisCacheManager(redisTemplate);
    }




    /**
     * 权限管理，配置主要是Realm的管理认证
     * 需要使用redis存储认证信息，所以，关闭session，重写缓存管理器
     * @param jwtRealm jwt认证管理
     * @return 安全管理器
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("jwtRealm") JwtRealm jwtRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> list = new ArrayList<>();
        list.add(jwtRealm);
        securityManager.setRealms(list);

        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        // 设置自定义Cache缓存
        securityManager.setCacheManager(shiroRedisCacheManager());

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        // 过滤规则
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/system/login/loginByUsernameAndPassword", "anon");
        filterChainDefinitionMap.put("/system/login/logout", "anon");
        filterChainDefinitionMap.put("/codeGenerator/generatorCode", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");

        // 自定义过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jwtFilter", new JwtFilter());
        factoryBean.setFilters(filters);
        filterChainDefinitionMap.put("/**", "jwtFilter");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }
}

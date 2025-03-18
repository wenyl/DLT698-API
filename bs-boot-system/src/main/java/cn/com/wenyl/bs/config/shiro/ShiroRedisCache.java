package cn.com.wenyl.bs.config.shiro;
import cn.com.wenyl.bs.config.jwt.JwtConstant;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Swimming Dragon
 * @description: shiro实现redis缓存
 * @date 2023年12月05日 16:50
 */
public class ShiroRedisCache implements Cache<Object, Object>{
    private final RedisTemplate<Object,Object> redisTemplate;
    public ShiroRedisCache(RedisTemplate<Object,Object> redisTemplate){
        super();
        this.redisTemplate = redisTemplate;
    }
    @Override
    public Object get(Object k) throws CacheException {
        return redisTemplate.opsForValue().get(cacheKey(k));
    }

    @Override
    public Object put(Object k, Object v) throws CacheException {
        redisTemplate.opsForValue().set(cacheKey(k),v, ShiroConstant.SHIRO_CACHE_EXPIRE_TIME, TimeUnit.MICROSECONDS);
        return v;
    }

    @Override
    public Object remove(Object k) throws CacheException {
        Object v = this.get(cacheKey(k));
        redisTemplate.opsForValue().getOperations().delete(cacheKey(k));
        return v;
    }

    @Override
    public void clear() throws CacheException {
        throw new UnsupportedOperationException("不支持清理redis操作");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("不支持获取redis键值对的大小操作");
    }

    @Override
    public Set<Object> keys() {
        throw new UnsupportedOperationException("不支持获取redis所有key操作");
    }

    @Override
    public Collection<Object> values() {
        throw new UnsupportedOperationException("不支持获取redis所有value操作");
    }

    private String cacheKey(Object key){
        return key.toString();
    }
}

package cn.com.wenyl.bs.config.shiro;


import lombok.Getter;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Swimming Dragon
 * @description: TODO
 * @date 2023年12月05日 16:44
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public  class ShiroRedisCacheManager implements CacheManager{
    private ConcurrentHashMap<String,Cache> caches = new ConcurrentHashMap<>();
    @Getter
    private final RedisTemplate<Object,Object> redisTemplate;
    public ShiroRedisCacheManager(RedisTemplate<Object,Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @Override
    public Cache getCache(String cacheName) throws CacheException {
        Cache cache = caches.get(cacheName);
        if(cache == null){
            cache = new ShiroRedisCache(getRedisTemplate());
            caches.put(cacheName,cache);
        }
        return cache;
    }
}

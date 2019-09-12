package com.hdw.upms.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @Description 实现shiro的CacheManager
 * @Author TuMingLong
 * @Date 2019/9/12 10:39
 */
@Component
public class ShiroCacheManager implements CacheManager {

    @Value("${hdw.shiro.cache}")
    private String shiroCacheKey;

    //30分钟过期
    @Value("${hdw.expire}")
    private int globExpire;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new ShiroCache<K, V>(shiroCacheKey, name, globExpire, redisTemplate);
    }

}
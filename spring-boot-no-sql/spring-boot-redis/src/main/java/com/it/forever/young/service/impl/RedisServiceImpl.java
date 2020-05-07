package com.it.forever.young.service.impl;

import com.it.forever.young.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanj566
 * @date 2019/12/6 1:32 PM
 **/
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Boolean ttl(Object key, long timeout, TimeUnit unit) {
        Assert.notNull(key, "cache key can not be null");
        if (null == unit) {
            unit = TimeUnit.MILLISECONDS;
        }
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Long getTtl(Object key, TimeUnit unit) {
        Assert.notNull(key, "cache key can not be null");
        if (null == unit) {
            unit = TimeUnit.MILLISECONDS;
        }
        return redisTemplate.getExpire(key, unit);
    }

    @Override
    public Boolean exist(Object key) {
        Assert.notNull(key, "cache key can not be null");
        return redisTemplate.hasKey(key);
    }

    @Override
    public Boolean delete(Object key) {
        Assert.notNull(key, "cache key can not be null");
        return redisTemplate.delete(key);
    }

    @Override
    public void set(Object key, Object value) {
        Assert.notNull(key, "cache key can not be null");
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Boolean setIfAbsent(Object key, Object value) {
        Assert.notNull(key, "cache key can not be null");
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public Boolean setIfPresent(Object key, Object value) {
        Assert.notNull(key, "cache key can not be null");
        return redisTemplate.opsForValue().setIfPresent(key, value);
    }

    @Override
    public void set(Object key, Object value, long timeout, TimeUnit unit) {
        Assert.notNull(key, "cache key can not be null");
        if (null == unit) {
            unit = TimeUnit.MILLISECONDS;
        }
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public Boolean setIfAbsent(Object key, Object value, long timeout, TimeUnit unit) {
        Assert.notNull(key, "cache key can not be null");
        if (null == unit) {
            unit = TimeUnit.MILLISECONDS;
        }
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    @Override
    public Boolean setIfPresent(Object key, Object value, long timeout, TimeUnit unit) {
        Assert.notNull(key, "cache key can not be null");
        if (null == unit) {
            unit = TimeUnit.MILLISECONDS;
        }
        return redisTemplate.opsForValue().setIfPresent(key, value, timeout, unit);
    }

    @Override
    public Long increment(Object key) {
        Assert.notNull(key, "cache key can not be null");
        return redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Long decrement(Object key) {
        Assert.notNull(key, "cache key can not be null");
        return redisTemplate.opsForValue().decrement(key);
    }

    @Override
    public Object get(Object key) {
        if (ObjectUtils.isEmpty(key)) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean hasHashKey(Object key, Object hashKey) {
        Assert.notNull(key, "cache key can not be null");
        Assert.notNull(hashKey, "hash key can not be null");
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Long deleteHashKey(Object key, Object hashKey) {
        Assert.notNull(key, "cache key can not be null");
        Assert.notNull(hashKey, "hash key can not be null");
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public void put(Object key, Object hashKey, Object value) {
        Assert.notNull(key, "cache key can not be null");
        Assert.notNull(hashKey, "hash key can not be null");
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void putAll(Object key, Map<Object, Object> kv) {
        Assert.notNull(key, "cache key can not be null");
        redisTemplate.opsForHash().putAll(key, kv);
    }

    @Override
    public Set<Object> keys(Object key) {
        Assert.notNull(key, "cache key can not be null");
        return redisTemplate.opsForHash().keys(key);
    }

    @Override
    public List<Object> values(Object key) {
        Assert.notNull(key, "cache key can not be null");
        return redisTemplate.opsForHash().values(key);
    }

    @Override
    public Object getHashValue(Object key, Object hashKey) {
        Assert.notNull(key, "cache key can not be null");
        if (ObjectUtils.isEmpty(hashKey)) {
            return null;
        }
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Boolean setBit(Object key, long position, boolean value) {
       return redisTemplate.opsForValue().setBit(key, position, value);
    }

    @Override
    public Boolean getBit(Object key, long position) {
        return redisTemplate.opsForValue().getBit(key, position);
    }
}

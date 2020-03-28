package com.it.forever.young.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanj566
 * @date 2019/12/6 1:32 PM
 **/
public interface RedisService {

    /** base operation */

    /**
     * get key ttl
     * @param key,
     * @param unit, default TimeUnit.MILLISECONDS
     * @return
     */
    Long getTtl(Object key, TimeUnit unit);

    /**
     * set key ttl
     * @param key
     * @param timeout, ttl time
     * @param unit, default TimeUnit.MILLISECONDS
     * @return
     */
    Boolean ttl(Object key, long timeout, TimeUnit unit);

    /**
     * whether key is exist
     * @param key
     * @return
     */
    Boolean exist(Object key);

    /**
     * delete key
     * @param key
     * @return
     */
    Boolean delete(Object key);

    /** Object type operation */

    /**
     * set value by key
     * @param key
     * @param value
     */
    void set(Object key, Object value);

    /**
     * set value by key if absent
     * @param key
     * @param value
     * @return
     */
    Boolean setIfAbsent(Object key, Object value);

    /**
     * set value by key if present
     * @param key
     * @param value
     * @return
     */
    Boolean setIfPresent(Object key, Object value);

    /**
     * set value by key with ttl
     * @param key
     * @param value
     * @param timeout
     * @param unit, default TimeUnit.MILLISECONDS
     */
    void set(Object key, Object value, long timeout, TimeUnit unit);

    /**
     * set value by key with ttl is absent
     * @param key
     * @param value
     * @param timeout
     * @param unit, default TimeUnit.MILLISECONDS
     * @return
     */
    Boolean setIfAbsent(Object key, Object value, long timeout, TimeUnit unit);

    /**
     * set value by key with ttl if present
     * @param key
     * @param value
     * @param timeout
     * @param unit, default TimeUnit.MILLISECONDS
     * @return
     */
    Boolean setIfPresent(Object key, Object value, long timeout, TimeUnit unit);

    /**
     * increment,
     * set value++ by key
     * @param key
     * @return
     */
    Long increment(Object key);

    /**
     * decrement
     * set value-- by key
     * @param key
     * @return
     */
    Long decrement(Object key);

    /**
     * get value by key
     * @param key
     * @return
     */
    Object get(Object key);

    /** list type operation */

    /** set type operation */

    /** zSet type operation */

    /** hash type operation */

    /**
     * whether hashKey is exist in key
     * @param key,
     * @param hashKey,
     * @return
     */
    Boolean hasHashKey(Object key, Object hashKey);

    /**
     * delete hashKey in key
     * @param key
     * @param hashKey
     * @return
     */
    Long deleteHashKey(Object key, Object hashKey);

    /**
     * put hashKey in key
     * <key, <hashKey, value>>
     * @param key
     * @param hashKey
     * @param value
     */
    void put(Object key, Object hashKey, Object value);

    /**
     * put multi hashKey in key
     * @param key
     * @param kv
     */
    void putAll(Object key, Map<Object, Object> kv);

    /**
     * get all hashKey in key
     * @param key
     * @return
     */
    Set<Object> keys(Object key);

    /**
     * get all hashValue in key
     * @param key
     * @return
     */
    List<Object> values(Object key);

    /**
     * get hashValue by hashKey in key
     * @param key
     * @param hashKey
     * @return
     */
    Object getHashValue(Object key, Object hashKey);

    /** bit map */

    /**
     *
     * @param key, key
     * @param position, position
     * @param value, if true, set this position 1, if false set this position 0
     * @return
     */
    Boolean setBit(Object key, long position, boolean value);

    Boolean getBit(Object key, long position);

}

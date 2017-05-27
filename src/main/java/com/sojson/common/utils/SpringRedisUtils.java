package com.sojson.common.utils;

import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * spring data redis 工具类
 * @author huangfei
 * @param <T>
 *
 */
@SuppressWarnings("rawtypes")
public class SpringRedisUtils<T> extends CacheTemplate{
	@Autowired
	private RedisTemplate<String, T> redisTemplate;
	
    public static RedisTemplate getRedisTemplate(){
    	return SpringContextUtil.getBean("redisTemplate",RedisTemplate.class);
    }
    
    public static void set(final String key, final Object value){
    	getRedisTemplate().opsForValue().set(key, value);
    }
    
    public static void set(final String key, String value) {
    	getRedisTemplate().opsForValue().set(key, value);
    }
    
    public static void setToJson(final String key, String value, Long time) {
    	getRedisTemplate().opsForValue().set(key, value);
    	expire(key, time);
    }
    
    public static <T> T getToJson(final String key, Class<T> elementType) {
        String jsonValue = (String) getRedisTemplate().opsForValue().get(key);
        return (T) JSONObject.toBean(JSONObject.fromObject(jsonValue), elementType);
//        return JsonUtils.jsonToObject(jsonValue, elementType);
    }

    public static boolean setNX(final String key, String value) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value);
    }
    
    public static boolean setNX(final String key, String value, Long time) {
    	boolean flag = getRedisTemplate().opsForValue().setIfAbsent(key, value);
    	expire(key, time);
        return flag;
    }

    public static <T> T getSet(final String key, String value, Class<T> clazz) {
        String oldValue = (String) getRedisTemplate().opsForValue().getAndSet(key, value);
        if (StringUtils.isEmpty(oldValue)) {
            return null;
        }
        return (T) JSONObject.toBean(JSONObject.fromObject(oldValue), clazz);
    }
    
    public static <T> T getSet(final String key, String value, Class<T> clazz, Long time) {
        String oldValue = (String) getRedisTemplate().opsForValue().getAndSet(key, value);
        if (StringUtils.isEmpty(oldValue)) {
            return null;
        }
        expire(key, time);
        return (T) JSONObject.toBean(JSONObject.fromObject(oldValue), clazz);
    }

    public static void delete(final String key) {
    	getRedisTemplate().delete(key);
    }
    
    public static boolean expire(String lockName, Long time, TimeUnit unit){
    	return getRedisTemplate().expire(lockName, time, unit);
    }
    
    public static boolean expire(String lockName, Long time){
    	return getRedisTemplate().expire(lockName, time, TimeUnit.SECONDS);
    }
    
    public static long increment(final String key, long expireTime) {
        return getRedisTemplate().opsForValue().increment(key, expireTime);
    }

}

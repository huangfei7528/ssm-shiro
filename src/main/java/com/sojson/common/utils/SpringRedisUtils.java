/*package com.sojson.common.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


*//**
 * spring data redis 工具类
 * @author huangfei
 * @param <T>
 *
 *//*
public class SpringRedisUtils<T>{
	
	@Value("${redis.prefix}")
	private String cachePrefix;
    
	public SpringRedisUtils() {
		super();
	}
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private RedisTemplate<String, T> redisTemplate;
	
    public static StringRedisTemplate getRedisTemplate(){
    	return  SpringContextUtil.getBean("stringRedisTemplate",StringRedisTemplate.class);
//    	return (StringRedisTemplate) getBean(.class);
    }
    
    public static void set(final String key, String value) {
    	getRedisTemplate().opsForValue().set(key, value);
    }
    
    public static void set(final String key, String value, Long time) {
    	getRedisTemplate().opsForValue().set(key, value);
    	expire(key, time);
    }
    
    public static <T> T get(final String key, Class<T> elementType) {
        String jsonValue = (String) getRedisTemplate().opsForValue().get(key);
        return JsonUtils.jsonToObject(jsonValue, elementType);
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
        return JsonUtils.jsonToObject(oldValue, clazz);
    }
    
    public static <T> T getSet(final String key, String value, Class<T> clazz, Long time) {
        String oldValue = (String) getRedisTemplate().opsForValue().getAndSet(key, value);
        if (StringUtils.isEmpty(oldValue)) {
            return null;
        }
        expire(key, time);
        return JsonUtils.jsonToObject(oldValue, clazz);
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

	public String getCachePrefix() {
		return cachePrefix;
	}

	public void setCachePrefix(String cachePrefix) {
		this.cachePrefix = cachePrefix;
	}
	
	*//**
	 * 获取整个项目的名称
	 * @return
	 *//*
	private String getGlobalPrjName(){
		return cachePrefix.split("_")[0];
	}

}
*/
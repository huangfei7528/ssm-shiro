package com.sojson.common.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;


/**
 * spring data redis 工具类
 * @author huangfei
 * @param <T>
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
    
    @SuppressWarnings("unchecked")
	public static void set(final Object key, final Object value){
    	getRedisTemplate().opsForValue().set(key, value);
    }
    
    @SuppressWarnings("unchecked")
	public static void set(final String key, final Object value){
    	getRedisTemplate().opsForValue().set(key, value);
    }
    
    @SuppressWarnings("unchecked")
	public static void set(final String key, final Object value, Long time){
    	getRedisTemplate().opsForValue().set(key, value);
    	expire(key, time);
    }
    
    public static Object get(final String key){
    	return getRedisTemplate().opsForValue().get(key);
    }
    
    public static Object get(final Object key){
    	return getRedisTemplate().opsForValue().get(key);
    }
    
    @SuppressWarnings("unchecked")
	public static void setToJson(final String key, String value, Long time) {
    	getRedisTemplate().opsForValue().set(key, value);
    	expire(key, time);
    }
    
    public static <T> T getToJson(final String key, Class<T> elementType) {
        String jsonValue = (String) getRedisTemplate().opsForValue().get(key);
        return JSONObject.toJavaObject(JSONObject.parseObject(jsonValue), elementType);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getObject(final String key, Class<T> clazz){
    	return  (T) getRedisTemplate().opsForValue().get(key);
    	
    }

    @SuppressWarnings("unchecked")
	public static boolean setNX(final String key, String value) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value);
    }
    
    @SuppressWarnings("unchecked")
	public static boolean setNX(final String key, String value, Long time) {
    	boolean flag = getRedisTemplate().opsForValue().setIfAbsent(key, value);
    	expire(key, time);
        return flag;
    }

    @SuppressWarnings("unchecked")
	public static <T> T getSet(final String key, String value, Class<T> clazz) {
        String oldValue = (String) getRedisTemplate().opsForValue().getAndSet(key, value);
        if (StringUtils.isEmpty(oldValue)) {
            return null;
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(oldValue), clazz);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getSet(final String key, String value, Class<T> clazz, Long time) {
        String oldValue = (String) getRedisTemplate().opsForValue().getAndSet(key, value);
        if (StringUtils.isEmpty(oldValue)) {
            return null;
        }
        expire(key, time);
        return JSONObject.toJavaObject(JSONObject.parseObject(oldValue), clazz);
    }

    @SuppressWarnings("unchecked")
	public static void delete(final String key) {
    	getRedisTemplate().delete(key);
    }
    
    @SuppressWarnings("unchecked")
  	public static void delete(final Object key) {
      	getRedisTemplate().delete(key);
      }
    
    @SuppressWarnings("unchecked")
	public static boolean expire(String lockName, Long time, TimeUnit unit){
    	return getRedisTemplate().expire(lockName, time, unit);
    }
    
    @SuppressWarnings("unchecked")
	public static boolean expire(String lockName, Long time){
    	return getRedisTemplate().expire(lockName, time, TimeUnit.SECONDS);
    }
    
    @SuppressWarnings("unchecked")
	public static long increment(final String key, long expireTime) {
        return getRedisTemplate().opsForValue().increment(key, expireTime);
    }

}

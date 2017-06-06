package com.sojson.common.utils;

import java.util.Map;
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
    /**
     * 设置缓存值 value方式
     * @param key 
     * @param value
     */
	public static void set(final String key, final Object value){
    	getRedisTemplate().opsForValue().set(getPreFix(key), value);
    }
    /**
     * 设置缓存值及缓存时间 value方式
     * @param key
     * @param value
     * @param time
     */
	public static void set(final String key, final Object value, Long time){
    	getRedisTemplate().opsForValue().set(getPreFix(key), value);
    	expire(key, time);
    }
    /**
     * 取出缓存值 value方式
     * @param key
     * @return
     */
    public static Object get(final String key){
    	return getRedisTemplate().opsForValue().get(getPreFix(key));
    }
    
    /**
     * 取出缓存值 hash方式
     * @param key
     * @param hashKey
     * @return
     */
    public static Object getHashKey(final String key, final String hashKey){
    	return getRedisTemplate().opsForHash().get(getPreFix(key), hashKey);
    }
    /**
     * 取出缓存值 hash方式
     * @param key
     * @param hashKey
     * @return
     */
    public static Object getHashKey(final String hashKey){
    	return getRedisTemplate().opsForHash().get(getPreFix(), hashKey);
    }
    /**
     * 设置缓存值 hash方式
     * @param key
     * @param hashKey
     * @param value
     */
    public static void setHashKey(final String key, final String hashKey, final Object value){
    	getRedisTemplate().opsForHash().put(getPreFix(key), hashKey, value);
    }
    public static void setHashKey(final String key, final String hashKey, final Object value, final Long time){
    	getRedisTemplate().opsForHash().put(getPreFix(key), hashKey, value);
    	expire(key, time);
    }
    /**
     * 取出缓存中key的值为map集合 hash方式
     * @param key
     * @return
     */
    public static Map<String, Object>  getKeyForMap(final String key){
 	   return getRedisTemplate().opsForHash().entries(getPreFix(key));
    }
    
	public static void setToJson(final String key, String value, Long time) {
    	getRedisTemplate().opsForValue().set(getPreFix(key), value);
    	expire(key, time);
    }
    
    public static <T> T getToJson(final String key, Class<T> elementType) {
        String jsonValue = (String) getRedisTemplate().opsForValue().get(getPreFix(key));
        return JSONObject.toJavaObject(JSONObject.parseObject(jsonValue), elementType);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getObject(final String key, Class<T> clazz){
    	return  (T) getRedisTemplate().opsForValue().get(getPreFix(key));
    	
    }

    @SuppressWarnings("unchecked")
	public static boolean setNX(final String key, String value) {
        return getRedisTemplate().opsForValue().setIfAbsent(getPreFix(key), value);
    }
    
    @SuppressWarnings("unchecked")
	public static boolean setNX(final String key, String value, Long time) {
    	boolean flag = getRedisTemplate().opsForValue().setIfAbsent(getPreFix(key), value);
    	expire(key, time);
        return flag;
    }

    @SuppressWarnings("unchecked")
	public static <T> T getSet(final String key, String value, Class<T> clazz) {
        String oldValue = (String) getRedisTemplate().opsForValue().getAndSet(getPreFix(key), value);
        if (StringUtils.isEmpty(oldValue)) {
            return null;
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(oldValue), clazz);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getSet(final String key, String value, Class<T> clazz, Long time) {
        String oldValue = (String) getRedisTemplate().opsForValue().getAndSet(getPreFix(key), value);
        if (StringUtils.isEmpty(oldValue)) {
            return null;
        }
        expire(key, time);
        return JSONObject.toJavaObject(JSONObject.parseObject(oldValue), clazz);
    }

    @SuppressWarnings("unchecked")
	public static void delete(final String key) {
    	getRedisTemplate().delete(getPreFix(key));
    }
    
    public static void deleteHashKey(final String key, final String hashKey){
    	getRedisTemplate().opsForHash().delete(getPreFix(key), hashKey);
    }
    
    @SuppressWarnings("unchecked")
  	public static void delete(final Object key) {
      	getRedisTemplate().delete(getPreFix(key));
      }
    
    @SuppressWarnings("unchecked")
	public static boolean expire(String lockName, Long time, TimeUnit unit){
    	return getRedisTemplate().expire(lockName, time, unit);
    }
    
    @SuppressWarnings("unchecked")
	public static boolean expire(String lockName, Long time){
    	return getRedisTemplate().expire(getPreFix(lockName), time, TimeUnit.SECONDS);
    }
    
    @SuppressWarnings("unchecked")
	public static long increment(final String key, long expireTime) {
        return getRedisTemplate().opsForValue().increment(getPreFix(key), expireTime);
    }
    
    public static String getPreFix(Object key){
    	if(key != null)
    		return "ssm-shiro" + ":" + key;
    	else
    		return "ssm-shiro" + ":";
	}
    
    public static String getPreFix(){
    	return getPreFix(null);
    }

}

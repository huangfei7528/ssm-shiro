package com.sojson.core.shiro.cache;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import com.alibaba.fastjson.JSONObject;
import com.sojson.common.utils.LoggerUtils;
import com.sojson.common.utils.SpringRedisUtils;

/**
 * 
 * 开发公司：sojson.com<br/>
 * 版权：sojson.com<br/>
 * <p>
 * 
 * 缓存获取Manager
 * 
 * <p>
 * 
 * 区分　责任人　日期　　　　说明<br/>
 * 创建　周柏成　2016年4月29日 　<br/>
 * <p>
 * *******
 * <p>
 * @author zhou-baicheng
 * @email  json@sojson.com
 * @version 1.0,2016年4月29日 <br/>
 * 
 */
@SuppressWarnings("unchecked")
public class JedisShiroCache<K, V> implements Cache<K, V> {

	/**
	 * 为了不和其他的缓存混淆，采用追加前缀方式以作区分
	 */
    private static final String REDIS_SHIRO_CACHE = "login-message-cache:";

//    private JedisManager jedisManager;
    
    private String name;

    
   /* public JedisShiroCache(String name, JedisManager jedisManager) {
        this.name = name;
        this.jedisManager = jedisManager;
    }*/
    
    public JedisShiroCache() {
       super();
    }

    /**
     * 自定义relm中的授权/认证的类名加上授权/认证英文名字
     */
    public String getName() {
        if (name == null)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public V get(K key) throws CacheException {
    	String cacheKey = buildCacheKey(key);
        try {
        	return (V) SpringRedisUtils.get(cacheKey);
        } catch (Exception e) {
            LoggerUtils.error(this.getClass(), "get value by cache throw exception",e);
        }
        return null;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V previos = get(key);
        try {
        	String cacheKey = buildCacheKey(key);
        	System.out.println(JSONObject.toJSONString(value));
            SpringRedisUtils.set(cacheKey, value);
        } catch (Exception e) {
        	 LoggerUtils.error(this.getClass(), "put cache throw exception",e);
        }
        return previos;
    }

    @Override
    public V remove(K key) throws CacheException {
        V previos = get(key);
        try {
            SpringRedisUtils.delete(buildCacheKey(key));
        } catch (Exception e) {
            LoggerUtils.error(this.getClass(), "remove cache  throw exception",e);
        }
        return previos;
    }

    @Override
    public void clear() throws CacheException {
        //TODO--
    }

    @Override
    public int size() {
        if (keys() == null)
            return 0;
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        //TODO
        return null;
    }

    @Override
    public Collection<V> values() {
        //TODO
        return null;
    }

    private String buildCacheKey(Object key) {
    	String prifix = REDIS_SHIRO_CACHE  + key;
        return prifix;
    }

}

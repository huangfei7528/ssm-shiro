package com.sojson.core.shiro.cache.impl;

import org.apache.shiro.cache.Cache;

import com.sojson.core.shiro.cache.JedisShiroCache;
import com.sojson.core.shiro.cache.ShiroCacheManager;

/**
 * 
 * 开发公司：SOJSON在线工具 <p>
 * 版权所有：© www.sojson.com<p>
 * 博客地址：http://www.sojson.com/blog/  <p>
 * <p>
 * 
 * JRedis管理
 * 
 * <p>
 * 
 * 区分　责任人　日期　　　　说明<br/>
 * 创建　周柏成　2016年6月2日 　<br/>
 *
 * @author zhou-baicheng
 * @email  so@sojson.com
 * @version 1.0,2016年6月2日 <br/>
 * 
 */
public class JedisShiroCacheManager implements ShiroCacheManager {


    @Override
	public <K, V> Cache<K, V> getCache(String name) {
//        return new JedisShiroCache<K, V>(name, getJedisManager());
        return new JedisShiroCache<K, V>();
    }

    @Override
    public void destroy() {
    	//如果和其他系统，或者应用在一起就不能关闭
    	//getJedisManager().getJedis().shutdown();
    }

}

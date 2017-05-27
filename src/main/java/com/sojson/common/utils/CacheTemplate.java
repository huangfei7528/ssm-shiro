package com.sojson.common.utils;

import org.springframework.beans.factory.annotation.Value;

public class CacheTemplate<T> {

	@Value("${redis.prefix}")
	private String cachePrefix;
	
	
	public CacheTemplate() {
		super();
	}

	public String getCachePrefix() {
		return cachePrefix;
	}

	public void setCachePrefix(String cachePrefix) {
		this.cachePrefix = cachePrefix;
	}
	
	/**
	 * 获取整个项目的名称
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getGlobalPrjName(){
		return cachePrefix.split("_")[0];
	}

	
}

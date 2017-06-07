package com.sojson.core.shiro.filter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;

/**
 * 解决shrio重写向报错bug重写 ShiroHttpServletResponse 的 encodeRedirectURL方法
 * @author huangfei
 *
 */
public class MyShiroHttpServletResponse extends ShiroHttpServletResponse {

	public MyShiroHttpServletResponse(HttpServletResponse wrapped,
			ServletContext context, ShiroHttpServletRequest request) {
		super(wrapped, context, request);
	}
	@Override
	public String encodeRedirectURL(String url) {
		return url;
	}

}

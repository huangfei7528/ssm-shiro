/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.hf.tomcat;

/**
 * 
 * @Filename PaycoreJettyBootStrap.java
 * 
 * @Description
 * 
 * @Version 1.0
 * 
 * @Author peigen
 * 
 * @Email peigen@yiji.com
 * 
 * @History <li>Author: peigen</li> <li>Date: 2012-9-3</li> <li>Version: 1.0</li>
 * <li>Content: create</li>
 * 
 */
public class AppStart {
	private static String dev = "dev";//开发环境
	private static String test = "test";//测试环境
	private static String online = "online";//线上环境
	public static void main(String[] args) throws Exception { 
		new TomcatBootstrapHelper(8084, false, dev, null).start();//参数：端口号， 是否启用servlet3.0， 环境标识， 项目名称 
		//new TomcatBootstrapHelper(8081, false).start();//参数：端口号， 是否启用servlet3.0
	}
}

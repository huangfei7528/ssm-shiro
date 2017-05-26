package com.hf.tomcat;

/*
 * 修订记录:
 * qzhanbo@yiji.com 2013-4-3 创建
 *
 */
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

import javax.servlet.ServletException;

/**
 * 嵌入式tomcat web应用启动类，为了避免测试时和线上的容器差别导致的问题，请优先使用此类。
 * <p>
 * 目录结构请参照标准的maven目录结构
 * </p>
 * <p>
 * 设置环境变量spring.profiles.active=dev
 * </p>
 * <h3>Usage Examples</h3>
 * <p/>
 * <li>1.maven增加依赖</li>
 * 
 * <pre>
 * {@code
 * 	<dependency>
 * 		<groupId>org.apache.tomcat.embed</groupId>
 * 		<artifactId>tomcat-embed-logging-juli</artifactId>
 * 		<version>7.0.39</version>
 * 		<scope>test</scope>
 * 	</dependency>
 * 	<dependency>
 * 		<groupId>org.apache.tomcat.embed</groupId>
 * 		<artifactId>tomcat-embed-jasper</artifactId>
 * 		<version>7.0.39</version>
 * 		<scope>test</scope>
 * 	</dependency>
 * }
 * 	</pre>
 * <li>2.编写启动类</li>
 * 
 * <pre>
 * 	public static void main(final String[] args) {
 * 		//传入监听端口
 * 		new TomcatBootstrapHelper(11111).start();
 *
 *    }
 * </pre>
 * 
 * @author Bohr.Qiu <qzhanbo@yiji.com>
 */
public class TomcatBootstrapHelper {
	private static final char ENTER_CHAR = '\n';
	/**
	 * 默认端口
	 */
	private static final int DEFAULT_PORT = 8080;
	private static final String DEFULT_ENV = "dev";
	private static final String DEFULT_NAME = "";
	private int port = DEFAULT_PORT;
	private String prjName = "";
	
	/**
	 * 是否启用servlet 3.0 支持，如果启用的话，就需要扫描jar包中是否有Servlet等annotation，这个会影响启动时间，默认不开启
	 */
	private boolean isServlet3Enable = false;
	
	/**
	 * 构建一个<code>TomcatBootstrapHelper.java</code>
	 * 
	 * @param port 端口
	 * @param isServlet3Enable 是否启用servlet 3.0
	 * 支持，如果启用的话，就需要扫描jar包中是否有Servlet等annotation，这个会影响启动时间，默认不开启
	 * @param env 设置环境变量system.env
	 */
	public TomcatBootstrapHelper(int port, boolean isServlet3Enable, String env, String prjName) {
		System.setProperty("system.env", env);
		this.port = port;
		if(prjName != null){
			this.prjName = prjName;
		}
		this.isServlet3Enable = isServlet3Enable;
	}
	
	/**
	 * 构建一个<code>TomcatBootstrapHelper.java</code><br>
	 * 环境变量spring.profiles.active=dev
	 * 
	 * @param port 端口
	 * @param isServlet3Enable 是否启用servlet 3.0
	 * 支持，如果启用的话，就需要扫描jar包中是否有Servlet等annotation，这个会影响启动时间，默认不开启
	 */
	public TomcatBootstrapHelper(int port, boolean isServlet3Enable) {
		this(port, isServlet3Enable, DEFULT_ENV, DEFULT_NAME);
	}
	
	/**
	 * 构建一个<code>TomcatBootstrapHelper.java</code><br>
	 * 不启用servlet 3.0 支持、环境变量spring.profiles.active=dev
	 * 
	 * @param port 端口
	 */
	public TomcatBootstrapHelper(int port) {
		this(port, false);
	}
	
	/**
	 * 构建一个<code>TomcatBootstrapHelper.java</code><br>
	 * 端口：8080、不启用servlet 3.0 支持、环境变量spring.profiles.active=dev
	 */
	public TomcatBootstrapHelper() {
		this(DEFAULT_PORT);
	}
	
	public void start() {
		try {
			long begin = System.currentTimeMillis();
			Tomcat tomcat = new Tomcat();
			configTomcat(tomcat);
			tomcat.start();
			long end = System.currentTimeMillis();
			log(end - begin);
			//在控制台回车就可以重启，提高效率
			while (true) {
				char c = (char) System.in.read();
				if (c == ENTER_CHAR) {
					begin = System.currentTimeMillis();
					System.out.println("重启tomcat...");
					tomcat.stop();
					tomcat.start();
					end = System.currentTimeMillis();
					log(end - begin);
				}
			}
		} catch (Exception e) {
			System.err.println("非常抱歉，貌似启动挂了...,请联系bohr");
			e.printStackTrace();
		}
		
	}
	
	private void configTomcat(final Tomcat tomcat) throws ServletException {
		//设置tomcat工作目录，maven工程里面就放到target目录下，看起来爽点，注意，这行代码不要随便移动位置，不然你可以have a try。
		tomcat.setBaseDir("target");
		tomcat.setPort(port);
		Connector connector = new Connector("HTTP/1.1");
		connector.setPort(port);
		connector.setURIEncoding("utf-8");
		tomcat.setConnector(connector);
		tomcat.getService().addConnector(connector);
		String webappPath = getWebappsPath();
		System.out.println("webapp目录：" + webappPath);
		Context ctx = tomcat.addWebapp(("".equals(this.prjName)?"":"/" + this.prjName), webappPath);//访问路径
		StandardJarScanner scanner = (StandardJarScanner) ctx.getJarScanner();
		if (!isServlet3Enable) {
			scanner.setScanAllDirectories(false);
			scanner.setScanClassPath(false);
		}
		tomcat.setSilent(true);
		System.setProperty("org.apache.catalina.SESSION_COOKIE_NAME", "JSESSIONID" + port);
	}
	
	private void log(long time) {
		System.out.println("********************************************************");
		System.out.println("启动成功: http://127.0.0.1:" + port + ("".equals(this.prjName)?"":"/" + this.prjName) + "   in:" + time + "ms");
		System.out.println("您可以直接在console里敲回车，重启tomcat,just have a try");
		System.out.println("********************************************************");
	}
	
	public String getWebappsPath() {
		String file = getClass().getClassLoader().getResource(".").getFile();
		return file.substring(0, file.indexOf("target")) + "WebContent";
	}
}

/**
 * 
 */
package com.cc.common.http.config;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 *
 */
@Configuration
@ConfigurationProperties(prefix="httpclient.config")
public class HttpClientConfig {

	/**
	 * 重试次数
	 */
	private int retryTime;
	
	/**
	 * 连接池最大连接数
	 */
	private int connMaxTotal = 20;
	
	/**
	 * 路由基数
	 */
	private int maxPerRoute = 2;
	
	/**
	 * 连接存活时间，单位s
	 */
	private int timeToLive = 60;
	
	/**
	 * 保持连接时间
	 */
	private int keepAliveTime = 30;
	
	/**
	 * 代理地址
	 */
	private String proxyHost="localhost";
	
	/**
	 * 代理端口
	 */
	private int proxyPort = 8080;
	
	/**
	 * 连接超时时间
	 */
	private int connectTimeout = 60000;
	
	/**
	 * 从连接池中取连接的超时时间
	 */
	private int connectRequestTimeout = 60000;
	
	/**
	 * 请求超时时间
	 */
	private int socketTimeout = 60000;
	
	/**
	 * 是否允许自动重定向
	 */
	private boolean redirectsEnabled = true;
	
	/**
	 * 重试处理机制
	 * @return
	 */
	@Bean
	public HttpRequestRetryHandler httpRequestRetryHandler(){
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler(){

			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				//重试次数超过了retryTime,则不再重试请求
				if (executionCount >= retryTime) {
					return false;
				}
				//服务端断掉客户端的连接异常
				if (exception instanceof NoHttpResponseException) {
					return true;
				}
				//超时重试
				if (exception instanceof InterruptedIOException) {
					return true;
				}
				//未知主机
				if (exception instanceof UnknownHostException) {
					return false;
				}
				//连接拒绝
				if (exception instanceof ConnectTimeoutException) {
					return false;
				}
				//ssl handshake 异常
				if (exception instanceof SSLException) {
					return false;
				}
				HttpClientContext clientContext = HttpClientContext.adapt(context);  
                HttpRequest request = clientContext.getRequest();  
                if (!(request instanceof HttpEntityEnclosingRequest)) {  
                    return true;  
                }
				return false;
			}
			
		};
		return httpRequestRetryHandler;
	}
	
	/**
	 * 连接池管理
	 * @return
	 */
	@Bean
	public PoolingHttpClientConnectionManager poolingClientConnectionManager(){
		PoolingHttpClientConnectionManager poolHttpcConnManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.SECONDS);
		poolHttpcConnManager.setMaxTotal(this.connMaxTotal);
		poolHttpcConnManager.setDefaultMaxPerRoute(this.maxPerRoute);
		return poolHttpcConnManager;
	}
	
	/**
	 * 连接保持策略
	 * @return
	 */
	@Bean
	public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
		ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {

			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while (it.hasNext()) { 
					HeaderElement he = it.nextElement();
					String param = he.getName();
					String value = he.getValue();
					if (value != null && param.equalsIgnoreCase("timeout")) {
						try {
							return Long.parseLong(value) * 1000;
						}catch (NumberFormatException ignore) {
						}
					}
				}
				return keepAliveTime * 1000;
			}
			
		};
		return connectionKeepAliveStrategy;
	}
	
	/**
	 * 代理
	 * @return
	 */
	//@Bean
	public DefaultProxyRoutePlanner defaultProxyRoutePlanner(){
		HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort);
		return new DefaultProxyRoutePlanner(proxy);
	}
	
	/**
	 * 请求配置
	 * @return
	 */
	@Bean
	public RequestConfig config(){
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectRequestTimeout)
				.setSocketTimeout(socketTimeout)
				.setRedirectsEnabled(redirectsEnabled)
				.build();
		return requestConfig;
	}
}

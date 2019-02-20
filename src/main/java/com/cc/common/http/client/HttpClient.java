/**
 * 
 */
package com.cc.common.http.client;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 *
 */
@Service
public class HttpClient implements FactoryBean<CloseableHttpClient>, InitializingBean, DisposableBean {

	/** 
     * 目标对象 
     */  
    private CloseableHttpClient client;  
      
    @Autowired(required=false)  
    private ConnectionKeepAliveStrategy connectionKeepAliveStrategy;  
      
    @Autowired(required=false)  
    private HttpRequestRetryHandler httpRequestRetryHandler;  
      
    @Autowired(required=false)  
    private DefaultProxyRoutePlanner proxyRoutePlanner;  
      
    @Autowired  
    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;  
      
    @Autowired(required=false)  
    private RequestConfig config;
    
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		if(null != this.client){ 
			this.client.close();  
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager);
		if (proxyRoutePlanner!=null) {
			httpClientBuilder.setRoutePlanner(proxyRoutePlanner);
		}
		if (httpRequestRetryHandler!=null) {
			httpClientBuilder.setRetryHandler(httpRequestRetryHandler);
		}
		if (connectionKeepAliveStrategy!=null) {
			httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy);
		}
		if (config!=null) {
			httpClientBuilder.setDefaultRequestConfig(config);
		}
		this.client = httpClientBuilder.build();
	}

	@Override
	public CloseableHttpClient getObject() throws Exception {
		return this.client;
	}

	@Override
	public Class<?> getObjectType() {
		return (this.client == null ? CloseableHttpClient.class : this.client.getClass());
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}

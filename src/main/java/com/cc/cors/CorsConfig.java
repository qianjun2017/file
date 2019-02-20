/**
 * 
 */
package com.cc.cors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Administrator
 *
 */
@Configuration
public class CorsConfig {

	@Bean
    public FilterRegistrationBean filterRegistrationBean() {
        // 对响应头进行CORS授权
		MyCorsRegistration corsRegistration = new MyCorsRegistration("*");
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("*");
        String[] objects = allowedOrigins.toArray(new String[allowedOrigins.size()]);

        corsRegistration
                //允许向该服务器提交请求的URI，*表示全部允许
                .allowedOrigins(objects)
                //允许提交请求的方法，*表示全部允许
                .allowedMethods(HttpMethod.DELETE.name(), HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.OPTIONS.name())
                //允许的头信息,*标识全部允许
                .allowedHeaders("*")
                //暴露的头信息
                .exposedHeaders(HttpHeaders.SET_COOKIE)
                //允许Cookie跨域，在做登录校验的时候有用
                .allowCredentials(true)
                //预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
                .maxAge(3600);

        // 注册CORS过滤器
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        //第一个参数表示过滤的url,*表示过滤所有
        configurationSource.registerCorsConfiguration("/**", corsRegistration.getCorsConfiguration());
        CorsFilter corsFilter = new CorsFilter(configurationSource);

        return new FilterRegistrationBean(corsFilter);
    }

}

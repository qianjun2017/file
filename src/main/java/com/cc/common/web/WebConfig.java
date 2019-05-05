/**
 * 
 */
package com.cc.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cc.common.spring.SpringContextUtil;
import com.cc.file.config.FileConfig;
import com.cc.file.strategy.FileStrategy;
import com.cc.file.strategy.impl.NativeFileStrategy;

/**
 * @author ws_yu
 *
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		FileStrategy fileStrategy = (FileStrategy)SpringContextUtil.getBean(FileStrategy.class);
		if(fileStrategy instanceof NativeFileStrategy){
			FileConfig fileConfig = (FileConfig)SpringContextUtil.getBean(FileConfig.class);
			registry.addResourceHandler("/files/**").addResourceLocations("file:"+fileConfig.getLocation());
		}
	}

}

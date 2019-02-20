/**
 * 
 */
package com.cc.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * spring上下文
 * @author Administrator
 *
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(SpringContextUtil.applicationContext == null){
			SpringContextUtil.applicationContext = applicationContext;
		}
	}

	/**
	 * 获取spring上下文
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	/**
	 * 获取bean
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName){
		return applicationContext.getBean(StringUtils.uncapitalize(beanName).replace("Bean",""));
	}
	
	/**
	 * 获取bean
	 * @param requiredType
	 * @return
	 */
	public static Object getBean(Class<?> requiredType){
		return applicationContext.getBean(requiredType);
	}
}

/**
 * 
 */
package com.cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Administrator
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages={"com.cc.system.*.mapper","com.cc.*.mapper","com.cc.system.*.dao","com.cc.*.dao"})
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}

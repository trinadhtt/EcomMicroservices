package com.ecom.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

@Configuration
public class FeignConfig {
	
	
	// Enables Logging using this bean
	//logging:level:com.ecom.order_service.client: DEBUG   add this  in .yml file 
	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

}

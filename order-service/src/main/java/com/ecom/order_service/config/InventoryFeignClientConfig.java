package com.ecom.order_service.config;

import java.time.Duration;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecom.order_service.exceptions.CustomErrorDecoder;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import feign.codec.ErrorDecoder;

@Configuration
public class InventoryFeignClientConfig {

	// used for log level configuration for Fiegn client by using the class name
	@Bean
	Logger.Level feignInvLoggerLevel() {
		return Logger.Level.FULL;
	}
	
	//Timeouts & retry Handling since its deprecated we can use them in application.yml
	/*
	 * cloud: openfeign: client: config: default: connectTimeout: 5000
	 * readTimeout: 5000 loggerLevel: basic
	 */
	  @Bean 
	  Request.Options options(){ 
	  return new Request.Options(Duration.ofMillis(3000),Duration.ofMillis(5000),true); 
	  }
	  
	  //long period(how much time will it wait), long maxPeriod(second time onwards how much time it has to wait),
	  //int maxAttempts
	  @Bean
	  public Retryer retryer() {
		  return new Retryer.Default(3l, 2l, 3);
	  }
	 
	// Request interceptors
	  @Bean
	  public RequestInterceptor requestInterceptor() {
		  return  RequestTemplate -> {
			  RequestTemplate.header("x-Correlation-Id", UUID.randomUUID().toString());
		  };
	  }
	  
	  //Custom error handling 
	  @Bean
	  public ErrorDecoder errorDecoder(){
		return new CustomErrorDecoder();
	  }
	  
	  
	  
}

package com.AutomationAPI;

import java.io.IOException;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentScan(basePackages= {"lithia.AutomationAPI"})
@SpringBootApplication
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	 //private static ConfigurableApplicationContext run;
	 


	public static void main(String[] args) 
	{
		
		//run=SpringApplication.run(Application.class, args);
		LOGGER.trace("for tracing purpose");
		LOGGER.debug("for debugging purpose");
		LOGGER.info("for informational purpose");
		LOGGER.warn("for warning purpose");
		LOGGER.error("for logging errors");
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		SpringApplication.run(Application.class, args);
		
	}
	


}

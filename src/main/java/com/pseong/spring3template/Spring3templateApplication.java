package com.pseong.spring3template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
public class Spring3templateApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring3templateApplication.class, args);
	}

}

package com.mindbowser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories(basePackages = "com.mindbowser.*")
@SpringBootApplication
@PropertySources(value = { @PropertySource("classpath:message.properties"),
		@PropertySource("classpath:exception.properties"), @PropertySource("classpath:sendgrid.properties") })
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

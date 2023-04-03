package com.ness.userprofileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ness.userprofileservice.config.RsaKeyProperties;


@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableJpaAuditing
public class UserprofileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserprofileServiceApplication.class, args);
	}
	
}

package com.ness.userprofileservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.User;

@EnableJpaAuditing
public class DataConfig {
	
	  @Bean
	  public AuditorAware<User> auditorProvider() {
	    return new AuditorAwareImpl();
	  }

}

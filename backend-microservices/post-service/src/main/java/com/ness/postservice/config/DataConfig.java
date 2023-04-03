package com.ness.postservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
public class DataConfig {
	
	  @Bean
	  public AuditorAware<String> auditorProvider() {
	    return new AuditorAwareImpl();
	  }

}

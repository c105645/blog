package com.ness.edgeservice.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;




@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private final RsaKeyProperties jwtConfigProperties;

	public SecurityConfig(RsaKeyProperties jwtConfigProperties) {
		this.jwtConfigProperties = jwtConfigProperties;
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
		 http.cors().configurationSource(corsConfigurationSource())
         .and()
         .csrf().disable()
 		.authorizeExchange(exchanges -> exchanges
		.pathMatchers("/resources/**", "/api/v1/userprofile/signup", "/api/v1/userprofile/auth/login", "/swagger-ui*/**", "/v3/api-docs/**").permitAll()
 		.anyExchange().authenticated()
 		)
 		.oauth2ResourceServer(OAuth2ResourceServerSpec::jwt);
	     
		 return http.build();
	}


	@Bean
	ReactiveJwtDecoder jwtDecoder() {
		return NimbusReactiveJwtDecoder.withPublicKey(jwtConfigProperties.publicKey()).build();
	}


	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("https://localhost:3000"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowedMethods(List.of("GET"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
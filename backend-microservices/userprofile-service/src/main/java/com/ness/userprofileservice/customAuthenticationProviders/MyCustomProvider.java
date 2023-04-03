package com.ness.userprofileservice.customAuthenticationProviders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ness.userprofileservice.exceptions.UserIdAndPasswordMismatchException;
import com.ness.userprofileservice.services.impl.MyUserDetailsService;




@Component
public class MyCustomProvider implements AuthenticationManager  {
	
	@Autowired
	 private MyUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	 public Authentication authenticate(Authentication authentication) throws AuthenticationException {	  
	  String providedUsername = authentication.getPrincipal().toString();
	  UserDetails user = userDetailsService.loadUserByUsername(providedUsername);
	  
	  
	      String providedPassword = authentication.getCredentials().toString();
	      String encryptedPassword = user.getPassword();
	      if(!passwordEncoder.matches(providedPassword, encryptedPassword))
	          throw new UserIdAndPasswordMismatchException("Incorrect Credentials");
	      
	      Authentication authenticationResult = 
	              new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
	      SecurityContext context = SecurityContextHolder.getContext();
	      context.setAuthentication(authenticationResult);
	      return authenticationResult;
	 
	 }
	}
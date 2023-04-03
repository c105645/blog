package com.ness.userprofileservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ness.userprofileservice.aspects.ToLog;
import com.ness.userprofileservice.dtos.LoginRequest;
import com.ness.userprofileservice.dtos.LoginResponse;
import com.ness.userprofileservice.dtos.UserProfileDto;
import com.ness.userprofileservice.exceptions.UserIdAndPasswordMismatchException;
import com.ness.userprofileservice.exceptions.UserNotFoundException;
import com.ness.userprofileservice.services.UserProfileService;
import com.ness.userprofileservice.services.impl.TokenService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping(AuthController.AUTH_API_ENDPOINT)
public class AuthController {
	
	 public static final String AUTH_API_ENDPOINT = "/api/v1/userprofile/auth";
	 
		private final AuthenticationManager manager;

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    
    private final TokenService tokenService;
	 private final UserProfileService service;


    public AuthController(TokenService tokenService, PasswordEncoder passwordEncoder,
	        AuthenticationManager manager, UserProfileService service) {
        this.tokenService = tokenService;
		this.manager = manager;
		this.service = service;
    }
    @ToLog
    @PostMapping("/login")
    public LoginResponse token(@RequestBody LoginRequest user) throws UserNotFoundException {
        LOG.info("Token requested for user: '{}'", user.username());
    	Authentication a = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        Authentication auth = manager.authenticate(a);
        if(auth.isAuthenticated() == false) {
        	throw new UserIdAndPasswordMismatchException("Incorrect Credentials");
        }
        String token = tokenService.generateToken(auth);
        UserProfileDto userdto = service.fetchByUsername(user.username());
        LOG.info("Token granted: {}", token);
        return new LoginResponse(userdto, token );
        
    }

}

package com.ness.postservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ness.postservice.aspects.ToLog;


@RestController
@RequestMapping(PostserviceController.POST_API_ENDPOINT)
public class PostserviceController {
	
	
	  public static final String POST_API_ENDPOINT = "/api/v1/post";
	  public static final String HELLO = "/hello";
	  public static final String COMMENT = "/comment";
	  
	  
		 @ToLog
		 @GetMapping(HELLO)
		 @ResponseStatus(HttpStatus.CREATED)
		 
		 public String registerUser() {
		    return "Hello World";     	
	   }
}

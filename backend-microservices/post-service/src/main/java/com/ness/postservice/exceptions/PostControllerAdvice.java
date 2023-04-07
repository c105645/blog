package com.ness.postservice.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class PostControllerAdvice {
	
	  @ExceptionHandler(PostNotFoundException.class)
      @ResponseStatus(HttpStatus.NOT_FOUND)
      public ResponseEntity<Object> postNotFoundHandler(PostNotFoundException ex, WebRequest req) {
       Map<String, Object> body = new LinkedHashMap<>();
          body.put("timestamp", LocalDateTime.now());
          body.put("message", ex.getMessage());

          return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
      }
	  
	  @ExceptionHandler(CommentNotFoundException.class)
      @ResponseStatus(HttpStatus.NOT_FOUND)
      public ResponseEntity<Object> commentNotFoundHandler(CommentNotFoundException ex, WebRequest req) {
       Map<String, Object> body = new LinkedHashMap<>();
          body.put("timestamp", LocalDateTime.now());
          body.put("message", ex.getMessage());

          return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
      }
	  
	  @ExceptionHandler(UserAlreadyVotedException.class)
      @ResponseStatus(HttpStatus.CONFLICT)
      public ResponseEntity<Object> userAlreadyVotedException(UserAlreadyVotedException ex, WebRequest req) {
       Map<String, Object> body = new LinkedHashMap<>();
          body.put("timestamp", LocalDateTime.now());
          body.put("message", ex.getMessage());

          return new ResponseEntity<>(body, HttpStatus.CONFLICT);
      }

}

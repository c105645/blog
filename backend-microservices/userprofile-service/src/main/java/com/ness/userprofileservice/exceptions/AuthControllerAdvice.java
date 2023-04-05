package com.ness.userprofileservice.exceptions;


import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

@ControllerAdvice
public class AuthControllerAdvice {       
           @ExceptionHandler(UserNotFoundException.class)
            @ResponseStatus(HttpStatus.NOT_FOUND)
            public ResponseEntity<Object> userNotFoundHandler(UserNotFoundException ex, WebRequest req) {
             Map<String, Object> body = new LinkedHashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("message", ex.getMessage());

                return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
            }

            @ExceptionHandler(UserAlreadyExistsException.class)
            @ResponseStatus(HttpStatus.CONFLICT)
            public ResponseEntity<Object>  userAlreadyExistsHandler(UserAlreadyExistsException ex, WebRequest req) {
                 Map<String, Object> body = new LinkedHashMap<>();
                    body.put("timestamp", LocalDateTime.now());
                    body.put("message", ex.getMessage());

                    return new ResponseEntity<>(body, HttpStatus.CONFLICT);
            }
            
            @ExceptionHandler(OperationNotAllowedException.class)
            @ResponseStatus(HttpStatus.FORBIDDEN)
            public ResponseEntity<Object>  operationNotAllowedHandler(OperationNotAllowedException ex, WebRequest req) {
                 Map<String, Object> body = new LinkedHashMap<>();
                    body.put("timestamp", LocalDateTime.now());
                    body.put("message", ex.getMessage());

                    return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
            }
            
            
            @ExceptionHandler(UserIdAndPasswordMismatchException.class)
            @ResponseStatus(HttpStatus.UNAUTHORIZED)
            public ResponseEntity<Object>  userIdAndPasswordMismatchException(UserIdAndPasswordMismatchException ex, WebRequest req) {
                
                    return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
            }
                    
            @ExceptionHandler(MethodArgumentNotValidException.class) 
            @ResponseStatus(HttpStatus.BAD_REQUEST)
            public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest req) {
                Map<String, Object> errors = new LinkedHashMap<String, Object>();
                errors.put("timestamp", LocalDateTime.now());
                ex.getBindingResult().getAllErrors().forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
                
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);     

            }


    }

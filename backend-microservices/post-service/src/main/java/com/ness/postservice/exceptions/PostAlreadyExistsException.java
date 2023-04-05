package com.ness.postservice.exceptions;

public class PostAlreadyExistsException extends Exception{

	private static final long serialVersionUID = 1L;

	public PostAlreadyExistsException(String message) {
        super(message);
    }
}

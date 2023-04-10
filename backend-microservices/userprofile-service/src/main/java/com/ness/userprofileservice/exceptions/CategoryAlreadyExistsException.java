package com.ness.userprofileservice.exceptions;

public class CategoryAlreadyExistsException extends Exception{

	private static final long serialVersionUID = 1L;

	public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}

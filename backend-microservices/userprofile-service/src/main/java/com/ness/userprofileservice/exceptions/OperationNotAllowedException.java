package com.ness.userprofileservice.exceptions;

public class OperationNotAllowedException extends Exception {
	private static final long serialVersionUID = 1L;

	public OperationNotAllowedException(String message) {
        super(message);
    }
}

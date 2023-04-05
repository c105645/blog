package com.ness.postservice.exceptions;

public class UserAlreadyVotedException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserAlreadyVotedException(String message) {
        super(message);
    }
}

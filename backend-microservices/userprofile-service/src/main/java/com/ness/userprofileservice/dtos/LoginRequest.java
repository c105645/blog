package com.ness.userprofileservice.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank(message="username cannot be blank")
		String username, 
		@NotBlank(message="password cannot be blank")
		String password) {

}

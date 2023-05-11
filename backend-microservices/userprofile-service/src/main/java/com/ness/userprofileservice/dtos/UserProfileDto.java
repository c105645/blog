package com.ness.userprofileservice.dtos;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@JsonInclude(Include.NON_NULL)
@Builder
public record UserProfileDto(
		Long id,
		@NotBlank(message = "user name cannot be blank") 
		@Size(message = "user name cannot be blank should be atleast 5 characters", min = 5) 
		String username,
		@Size(message = "First name cannot be blank and should be atleast 5 characters", min = 5) 
		String firstName,
		String lastName,
		@NotBlank(message = "password should be supplied") 
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
		String password,
		@NotBlank(message = "Email should be provided") 
		@Email(message = "Enter a valid email") 
		String email,
		boolean isAdmin, 
		boolean isAuthor,
		String biography, 
		String imageUrl, 
		List<CategoryDto> categories,
		List<UserProfileDto> followers,
		List<UserProfileDto> following, 
		LocalDate createdAt, 
		LocalDate updatedAt) {
}

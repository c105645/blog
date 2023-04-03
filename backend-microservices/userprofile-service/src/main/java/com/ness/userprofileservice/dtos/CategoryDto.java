package com.ness.userprofileservice.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

public record CategoryDto(
		Long id,
		@Size(message = "name cannot be blank and should be atleast 5 characters", min = 5)
		String name,
		@Size(message = "description cannot be blank and should be atleast 5 characters", min = 5)
		String description,
		LocalDate createdAt, 
		LocalDate updatedAt
		) {}

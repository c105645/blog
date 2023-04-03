package com.ness.postservice.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagDto(
		Long id,
		@NotBlank(message = "tag name cannot be blank") 
		String name,
		@Size(message = "description cannot be blank and should be atleast 25 characters", min = 5) 
		String description,
		String createdBy,
		LocalDate createdAt, 
		LocalDate updatedAt
		) {}

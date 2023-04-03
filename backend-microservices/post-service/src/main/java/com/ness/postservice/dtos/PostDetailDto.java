package com.ness.postservice.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostDetailDto(
		Long id,
		@NotBlank(message="content cannot be blank")
		@Size(message = "content should be atleast 50 characters", min = 50)
		String content,
		PostDto post,
		String imageUrl,
		LocalDate createdAt,
		LocalDate updatedAt,
		String createdBy
) {

}

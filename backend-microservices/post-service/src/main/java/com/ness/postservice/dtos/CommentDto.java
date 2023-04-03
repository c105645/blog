package com.ness.postservice.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CommentDto(
		Long id,
		@NotBlank(message="review cannot be blank")
		PostDto post,
		String review,
		String createdBy,
	    LocalDate createdAt,
	    LocalDate updatedAt,
	    int upVoteCount,
	    int downVoteCount) {

}

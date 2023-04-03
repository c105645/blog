package com.ness.postservice.dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record PostDto(
		Long id,
		@NotBlank(message="title cannot be blank")
		String title,
		@NotBlank(message="description cannot be blank")
		String short_description,
		PostDetailDto postDetails,
		List<CommentDto> comments,
		@NotBlank(message="category cannot be blank")
		String category,
		List<TagDto> tags,
	    LocalDate createdAt,
	    LocalDate updatedAt,
	    String createdBy,
	    int upVoteCount,
	    int downVoteCount,
	    int commentCount
	    ) {

}

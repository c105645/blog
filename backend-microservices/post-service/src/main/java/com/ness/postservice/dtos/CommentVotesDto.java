package com.ness.postservice.dtos;

import java.time.LocalDate;

public record CommentVotesDto(
		Long id,
		int score,
		CommentDto comment,
		String createdBy,
		LocalDate createdAt,
		LocalDate updatedAt) {

}

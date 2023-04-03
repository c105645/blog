package com.ness.postservice.dtos;

import java.time.LocalDate;

public record PostVotesDto(
		Long id,
		int score,
		PostDto post,
		String createdBy,
		LocalDate createdAt,
		LocalDate updatedAt) {

}

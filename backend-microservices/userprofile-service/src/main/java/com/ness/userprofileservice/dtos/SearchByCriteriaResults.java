package com.ness.userprofileservice.dtos;

import java.util.List;

import lombok.Builder;
@Builder
public record SearchByCriteriaResults(
		String searchBy, 
		String searchString,
		List<UserProfileDto> authors,
		List<CategoryDto> categories) {
}

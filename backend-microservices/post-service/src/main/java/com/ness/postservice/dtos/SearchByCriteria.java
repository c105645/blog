package com.ness.postservice.dtos;

import java.util.List;

public record SearchByCriteria(
		String searchBy, 
		String searchString, 
		List<String> following, 
		List<String> categories) {}

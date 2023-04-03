package com.ness.userprofileservice.dtos;

import jakarta.validation.constraints.NotNull;

public record FollowRequest(
		@NotNull(message="userId cannot be blank")
		Long userId, 
		@NotNull(message="toFollow userId cannot be blank")
		Long toFollowId) {

}

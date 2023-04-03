package com.ness.userprofileservice.dtos;

import jakarta.validation.constraints.NotNull;

public record UnFollowRequest(
		@NotNull(message="userId cannot be blank")
		Long userId, 
		@NotNull(message="toUnFollow userId cannot be blank")
		Long toUnFollowId) {

}

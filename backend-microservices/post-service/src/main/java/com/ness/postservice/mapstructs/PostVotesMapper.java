package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ness.postservice.dtos.PostVotesDto;
import com.ness.postservice.entities.PostVotes;

@Mapper(componentModel = "spring")
public interface PostVotesMapper {

	PostVotesDto toDto(PostVotes vote);
	
	PostVotes toEntity(PostVotesDto vote);
}

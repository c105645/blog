package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ness.postservice.dtos.CommentVotesDto;
import com.ness.postservice.entities.CommentVotes;

@Mapper(componentModel = "spring")
public interface CommentVotesMapper {
	
	@Mapping(target = "comment", ignore = true)
	CommentVotesDto toDto(CommentVotes vote);
	
	CommentVotes toEntity(CommentVotesDto vote);

}

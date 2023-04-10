package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ness.postservice.dtos.CommentDto;
import com.ness.postservice.entities.Comment;


@Mapper(componentModel = "spring")
public interface CommentMapper {
	
	CommentDto toDto(Comment comment);
	
	@Mapping(target = "post", ignore = true)
	Comment toEntity(CommentDto comment);

}

package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.entities.Post;

@Mapper(componentModel = "spring", uses = { CommentMapper.class, PostDetailMapper.class })
public interface PostsListMapper {

	@Mapping(target = "postDetails", ignore = true)
	@Mapping(target = "comments", ignore = true)
	PostDto toDto(Post post);
	
}

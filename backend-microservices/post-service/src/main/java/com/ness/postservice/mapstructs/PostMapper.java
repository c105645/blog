package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.entities.Post;
		
@Mapper(componentModel = "spring", uses= {CommentMapper.class})
public interface PostMapper {
	
	@Mapping(target = "postDetails", ignore = true)
	PostDto toDto(Post post);
	
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	Post toEntity(PostDto post);

}

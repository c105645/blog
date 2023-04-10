package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;

import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.entities.Post;
		
@Mapper(componentModel = "spring", uses= {CommentMapper.class, PostDetailMapper.class})
public interface PostMapper {
	
	PostDto toDto(Post post);
	
	Post toEntity(PostDto post);
}

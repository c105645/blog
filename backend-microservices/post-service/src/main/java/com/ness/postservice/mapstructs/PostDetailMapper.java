package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ness.postservice.dtos.PostDetailDto;
import com.ness.postservice.entities.PostDetails;

@Mapper(componentModel = "spring")
public interface PostDetailMapper {

	PostDetailDto toDto(PostDetails postdetail);
	
	PostDetails toEntity(PostDetailDto postdetail);


}

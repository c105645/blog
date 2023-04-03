package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ness.postservice.dtos.PostDetailDto;
import com.ness.postservice.entities.PostDetails;

@Mapper(componentModel = "spring")
public interface PostDetailMapper {

	@Mapping(target = "post", ignore = true)
	PostDetailDto toDto(PostDetails postdetail);
	
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	PostDetails toEntity(PostDetailDto postdetail);


}

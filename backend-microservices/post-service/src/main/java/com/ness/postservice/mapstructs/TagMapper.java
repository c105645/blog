package com.ness.postservice.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ness.postservice.dtos.TagDto;
import com.ness.postservice.entities.Tag;


@Mapper(componentModel = "spring")
public interface TagMapper {
	
	TagDto toDto(Tag tag);
	
	Tag toEntity(TagDto tag);

}

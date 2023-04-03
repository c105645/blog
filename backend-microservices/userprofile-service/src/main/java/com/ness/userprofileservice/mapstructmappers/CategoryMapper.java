package com.ness.userprofileservice.mapstructmappers;

import org.mapstruct.Mapper;

import com.ness.userprofileservice.dtos.CategoryDto;
import com.ness.userprofileservice.entities.Category;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
 
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Category toEntity(CategoryDto category);
    CategoryDto toDto(Category category);
}

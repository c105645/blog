package com.ness.userprofileservice.mapstructmappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.ness.userprofileservice.dtos.UserProfileDto;
import com.ness.userprofileservice.entities.UserProfileEntity;

@Mapper(componentModel = "spring", uses=CategoryMapper.class)
public interface UserProfileMapper {
	
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "categories", ignore = true)
	UserProfileEntity toEntity(UserProfileDto user);
     
	@Mapping( target = "followers", qualifiedByName = "followerToDto")
	@Mapping( target = "following", qualifiedByName = "followerToDto")
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
 	UserProfileDto toDto(UserProfileEntity user);

	
	@Named("followerToDto")
    default UserProfileDto followerToDto(UserProfileEntity entity) {
		return UserProfileDto.builder()
							.id(entity.getId())
							.username(entity.getUsername())
							.firstName(entity.getFirstName())
							.lastName(entity.getLastName())
							.biography(entity.getBiography())
							.email(entity.getEmail())
							.imageUrl(entity.getImageUrl())
							.build();
    }
	
	

}

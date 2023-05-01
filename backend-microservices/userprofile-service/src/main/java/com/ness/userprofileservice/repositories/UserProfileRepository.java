package com.ness.userprofileservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ness.userprofileservice.entities.UserProfileEntity;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long>{
	
	@Query("select u from UserProfileEntity u where u.username=?1")
	public Optional<UserProfileEntity> findUserProfileByUsername(String username);
		
	public Optional<UserProfileEntity> findUserProfileByEmail(String email);

	@Query("select u from UserProfileEntity u where u.username=?1 or u.email=?2")
    public Optional<UserProfileEntity> findUserProfileByUserNameOrEmail(String username, String email);



}

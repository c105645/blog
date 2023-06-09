package com.ness.userprofileservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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


	@Query("select u from UserProfileEntity u where u.username in ?1")
	public Page<UserProfileEntity> findAllByUsername(List<String> users, Pageable firstPage);
	
	@Query("select u from UserProfileEntity u where lower(u.username) LIKE lower(concat('%', concat(?1, '%'))) or lower(u.firstName) LIKE lower(concat('%', concat(?1, '%'))) or lower(u.lastName) LIKE lower(concat('%', concat(?1, '%'))) or lower(u.email) LIKE lower(concat('%', concat(?1, '%'))) or lower(u.biography) LIKE lower(concat('%', concat(?1, '%')))")
	Slice<UserProfileEntity> findAllBySearchString(String searchString, Pageable firstPage);
	

	 @Query("select u from UserProfileEntity u left join u.categories c where c.id = ?1")
	 List<UserProfileEntity> findUserWithCategoery(Long categoeryId);

}

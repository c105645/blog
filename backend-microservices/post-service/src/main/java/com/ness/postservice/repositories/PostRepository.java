package com.ness.postservice.repositories;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	Page<Post> findAllByCategory(String category, Pageable page);

	@Query("select p from Post p where p.createdBy in ?1")
	Page<Post> findAllByFollowing(List<String> following, Pageable firstPage);

	@Query("select p from Post p where p.createdBy = ?1")
	Slice<Post> findAllByCreatedBy(String author, Pageable firstPage);
	
	@Query("select p from Post p where lower(p.createdBy) LIKE lower(concat('%', concat(?1, '%'))) or lower(p.category) LIKE lower(concat('%', concat(?1, '%')))")
	Slice<Post> findAllBySearchString(String searchString, Pageable firstPage);
	
}
	
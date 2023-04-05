package com.ness.postservice.repositories;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	
	Page<Post> findByCategory(String category, Pageable page);

	@Query("select p from Post p where p.createdBy=?1")
	Page<Post> findByCreatedBy(String user, Pageable firstSortedPage);
	
	
	@Query("select p from Post p where p.createdBy=?1")
	List<Post> findAllByCreatedBy(String user);
	
	
}

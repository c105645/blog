package com.ness.postservice.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	Page<Post> findAllByCategory(String category, Pageable page);

}

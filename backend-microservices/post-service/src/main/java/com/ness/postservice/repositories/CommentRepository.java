package com.ness.postservice.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ness.postservice.entities.Comment;
import com.ness.postservice.entities.Post;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

	@Query("select c from Comment c where c.post=?1")
	Page<Comment> findAllCommentsByPost(Post post, Pageable pageable);
	
	@Query("select c from Comment c where c.post=?1 and c.createdBy=?2")
	Page<Comment> findAllByCreatedByAndPost(Post post, String user, Pageable pageable);
	
	@Query("select c from Comment c where c.post=?2 and c.createdBy=?1 limit=1")
	Optional<Comment> findOneByCreatedByAndPost(String user, Post post);
}

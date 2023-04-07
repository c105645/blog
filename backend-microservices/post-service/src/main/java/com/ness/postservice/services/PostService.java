package com.ness.postservice.services;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.exceptions.PostNotFoundException;

public interface PostService {

	PostDto getPostById(long id) throws PostNotFoundException;

	List<PostDto> getPostListByPage(Pageable pageable);
	
	List<PostDto> getMostVotedPosts(Pageable pageable);
	
	List<PostDto> getMostCommentedPosts(Pageable pageable);
	
	List<PostDto> getPostsCreatedByCurrentUser(Pageable pageable);
	
	List<PostDto> getMostCommentedPostsByCategory(String category, Pageable pageable);
	
	List<PostDto> getMostVotedPostsByCategory(String category, Pageable pageable);
	
	PostDto create(PostDto post);

	PostDto update(PostDto post);

	void delete(Long id) throws PostNotFoundException;
}

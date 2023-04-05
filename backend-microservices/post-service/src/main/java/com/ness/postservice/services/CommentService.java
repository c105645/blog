package com.ness.postservice.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.ness.postservice.dtos.CommentDto;
import com.ness.postservice.exceptions.CommentNotFoundException;
import com.ness.postservice.exceptions.PostNotFoundException;

public interface CommentService {
	
	  CommentDto create(CommentDto comment);

	  CommentDto update(CommentDto comment);
	  
	  void delete(Long commentid)throws CommentNotFoundException;

	  CommentDto getCommentById(long id)throws CommentNotFoundException;

	  List<CommentDto> getCommentListOfPostByPage(Long PostId, Pageable pageable) throws PostNotFoundException;
	  
	  boolean isPostCommentedByCurrentUser(Long PostId)  throws PostNotFoundException;
	  
	  List<CommentDto> getMostVotedCommentsOfaPost(Long PostId, Pageable pageable) throws PostNotFoundException;

}

package com.ness.postservice.services;

import java.util.List;

import com.ness.postservice.dtos.CommentDto;
import com.ness.postservice.dtos.CommentVotesDto;
import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.dtos.PostVotesDto;

public interface VotesService {
	
	  PostVotesDto createPostVote(PostVotesDto postVote);

	  CommentVotesDto createCommentVote(CommentVotesDto commentVote);

	  PostVotesDto upVotePostById(Long id);

	  PostVotesDto downVotePostById(Long id);

	  CommentVotesDto upVoteCommentById(Long id);

	  CommentVotesDto downVoteCommentById(Long id);

	  boolean isPostVotedByCurrentUser(Long postid);

	  boolean isCommentVotedByCurrentUser(Long commentid);

	  List<PostDto> getPostsVotedByCurrentUser();

	  List<CommentDto> getCommentsVotedByCurrentUser();

}

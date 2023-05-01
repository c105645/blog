package com.ness.postservice.services;



import com.ness.postservice.dtos.CommentVotesDto;
import com.ness.postservice.dtos.PostVotesDto;
import com.ness.postservice.exceptions.CommentNotFoundException;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.exceptions.UserAlreadyVotedException;

public interface VotesService {
	
	
	  PostVotesDto upVotePostById(Long id) throws PostNotFoundException, UserAlreadyVotedException;

	  PostVotesDto downVotePostById(Long id) throws PostNotFoundException, UserAlreadyVotedException;

	  CommentVotesDto upVoteCommentById(Long id) throws CommentNotFoundException, UserAlreadyVotedException;

	  CommentVotesDto downVoteCommentById(Long id) throws CommentNotFoundException, UserAlreadyVotedException;

	  int isPostVotedByCurrentUser(Long postid) throws PostNotFoundException; //-1 downvoted; 1 upvoted 0 not voted

	  int isCommentVotedByCurrentUser(Long commentid) throws CommentNotFoundException;//-1 downvoted; 1 upvoted 0 not voted
	  void deletePostVotes(Long PostId) throws PostNotFoundException;
	  void deleteCommentVotes(Long CommentId) throws CommentNotFoundException;


}

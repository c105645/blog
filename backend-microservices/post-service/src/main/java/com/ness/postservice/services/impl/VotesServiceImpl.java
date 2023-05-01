package com.ness.postservice.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ness.postservice.dtos.CommentVotesDto;
import com.ness.postservice.dtos.PostVotesDto;
import com.ness.postservice.entities.Comment;
import com.ness.postservice.entities.CommentVotes;
import com.ness.postservice.entities.Post;
import com.ness.postservice.entities.PostVotes;
import com.ness.postservice.exceptions.CommentNotFoundException;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.exceptions.UserAlreadyVotedException;
import com.ness.postservice.mapstructs.CommentVotesMapper;
import com.ness.postservice.mapstructs.PostVotesMapper;
import com.ness.postservice.repositories.CommentRepository;
import com.ness.postservice.repositories.CommentVotesRepository;
import com.ness.postservice.repositories.PostRepository;
import com.ness.postservice.repositories.PostVotesRepository;
import com.ness.postservice.services.VotesService;

import jakarta.transaction.Transactional;

@Service
public class VotesServiceImpl implements VotesService {

	private final CommentRepository commentrepo;
	private final PostRepository postrepo;
	private final PostVotesRepository postvotesrepo;
	private final CommentVotesRepository commentvotesrepo;

	private final PostVotesMapper postvotesmapper;
	private final CommentVotesMapper commentvotesmapper;

	public VotesServiceImpl(PostRepository postrepo, CommentRepository commentrepo, PostVotesRepository postvotesrepo,
			CommentVotesRepository commentvotesrepo, PostVotesMapper postvotesmapper,
			CommentVotesMapper commentvotesmapper) {

		this.postvotesmapper = postvotesmapper;
		this.commentvotesmapper = commentvotesmapper;

		this.postrepo = postrepo;
		this.commentrepo = commentrepo;
		this.postvotesrepo = postvotesrepo;
		this.commentvotesrepo = commentvotesrepo;
	}

	@Override
	@Transactional()
	public PostVotesDto upVotePostById(Long postId) throws UserAlreadyVotedException, PostNotFoundException {
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();

		Optional<PostVotes> voteopt = postvotesrepo.findIfUserHasAlreadyVoted(post, user);

		if (voteopt.isEmpty()) {
			PostVotes postvote = new PostVotes();
			postvote.setPost(post);
			postvote.setScore(1);
			post.increaseUpVoteCount();
			postrepo.save(post);
			return postvotesmapper.toDto(postvotesrepo.save(postvote));
		} else {
			var vote = voteopt.get();
			if (vote.getScore() == -1) {
				vote.setScore(1);
				post.increaseUpVoteCount();
				post.decreaseDownVoteCount();
				postrepo.save(post);
				return postvotesmapper.toDto(postvotesrepo.save(vote));
			} else {
				throw new UserAlreadyVotedException("Post with Id " + postId + " has already been up voted");
			}
		}
	}

	@Override
	@Transactional()
	public PostVotesDto downVotePostById(Long postId) throws PostNotFoundException, UserAlreadyVotedException {
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();

		Optional<PostVotes> voteopt = postvotesrepo.findIfUserHasAlreadyVoted(post, user);

		if (voteopt.isEmpty()) {
			PostVotes postvote = new PostVotes();
			postvote.setPost(post);
			postvote.setScore(1);
			post.increaseDownVoteCount();
			postrepo.save(post);
			return postvotesmapper.toDto(postvotesrepo.save(postvote));
		} else {
			var vote = voteopt.get();
			if (vote.getScore() == 1) {
				vote.setScore(-1);
				post.increaseDownVoteCount();
				post.decreaseUpVoteCount();
				postrepo.save(post);
				return postvotesmapper.toDto(postvotesrepo.save(vote));
			} else {
				throw new UserAlreadyVotedException("Post with Id " + postId + " has already been down voted");
			}
		}
	}

	@Override
	@Transactional()
	public CommentVotesDto upVoteCommentById(Long commentId)
			throws CommentNotFoundException, UserAlreadyVotedException {
		System.out.println("commentId: " +  commentId);
		Comment comment = commentrepo.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " doesnot exists"));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();

		Optional<CommentVotes> voteopt = commentvotesrepo.findIfUserHasAlreadyVoted(comment, user);

		if (voteopt.isEmpty()) {
			CommentVotes commentvote = new CommentVotes();
			commentvote.setComment(comment);
			commentvote.setScore(1);
			comment.increaseUpVoteCount();
			commentrepo.save(comment);
			return commentvotesmapper.toDto(commentvotesrepo.save(commentvote));
		} else {
			var vote = voteopt.get();
			if (vote.getScore() == -1) {
				vote.setScore(1);
				comment.increaseUpVoteCount();
				comment.decreaseDownVoteCount();
				commentrepo.save(comment);
				return commentvotesmapper.toDto(commentvotesrepo.save(vote));
			} else {
				throw new UserAlreadyVotedException("Comment with Id " + commentId + " has already been up voted");
			}
		}
	}

	@Override
	@Transactional()
	public CommentVotesDto downVoteCommentById(Long commentId)
			throws CommentNotFoundException, UserAlreadyVotedException {
		Comment comment = commentrepo.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " doesnot exists"));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();

		Optional<CommentVotes> voteopt = commentvotesrepo.findIfUserHasAlreadyVoted(comment, user);

		if (voteopt.isEmpty()) {
			CommentVotes commentvote = new CommentVotes();
			commentvote.setComment(comment);
			commentvote.setScore(-1);
			comment.increaseDownVoteCount();
			commentrepo.save(comment);
			return commentvotesmapper.toDto(commentvotesrepo.save(commentvote));
		} else {
			var vote = voteopt.get();
			if (vote.getScore() == 1) {
				vote.setScore(-1);
				comment.increaseDownVoteCount();
				comment.decreaseUpVoteCount();
				commentrepo.save(comment);
				return commentvotesmapper.toDto(commentvotesrepo.save(vote));
			} else {
				throw new UserAlreadyVotedException("Comment with Id " + commentId + " has already been downvoted");
			}
		}

	}

	@Override
	@Transactional()
	public int isPostVotedByCurrentUser(Long postId) throws PostNotFoundException {
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<PostVotes> vote = postvotesrepo.findIfUserHasAlreadyVoted(post, user);
		if (vote.isEmpty())
			return 0;
		return vote.get().getScore();
	}

	@Override
	@Transactional()
	public int isCommentVotedByCurrentUser(Long commentId) throws CommentNotFoundException {
		Comment comment = commentrepo.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " doesnot exists"));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<CommentVotes> vote = commentvotesrepo.findIfUserHasAlreadyVoted(comment, user);
		if (vote.isEmpty())
			return 0;
		return vote.get().getScore();
	}

	@Override
	@Transactional()
	public void deletePostVotes(Long postId) throws PostNotFoundException {
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		List<PostVotes> votes = postvotesrepo.findAllByPost(post);
		postvotesrepo.deleteAll(votes);
	}

	@Override
	@Transactional()
	public void deleteCommentVotes(Long commentId) throws CommentNotFoundException {
		Comment comment = commentrepo.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " doesnot exists"));
		List<CommentVotes> votes = commentvotesrepo.findAllByComment(comment);
		commentvotesrepo.deleteAll(votes);
		
	}
	
	
}

package com.ness.postservice.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ness.postservice.dtos.CommentDto;
import com.ness.postservice.entities.Comment;
import com.ness.postservice.entities.Post;
import com.ness.postservice.exceptions.CommentNotFoundException;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.mapstructs.CommentMapper;
import com.ness.postservice.repositories.CommentRepository;
import com.ness.postservice.repositories.PostRepository;
import com.ness.postservice.services.CommentService;

public class CommentServiceImpl implements CommentService{

	private final CommentRepository repo;
	private final PostRepository postrepo;

	private final CommentMapper mapper;

	public CommentServiceImpl(CommentRepository repo, CommentMapper mapper, PostRepository postrepo) {
		this.repo = repo;
		this.mapper = mapper;
		this.postrepo=postrepo;
	}

	@Override
	public CommentDto getCommentById(long id) throws CommentNotFoundException {
		Comment comment = repo.findById(id)
				.orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " doesnot exists"));
		return mapper.toDto(comment);
	}

	@Override
	public CommentDto create(CommentDto comment) {
		Comment com =  repo.save(mapper.toEntity(comment));
		return mapper.toDto(com);
	}

	@Override
	public CommentDto update(CommentDto comment) {
		Comment com =  repo.save(mapper.toEntity(comment));
		return mapper.toDto(com);
	}

	@Override
	public void delete(Long commentid) throws CommentNotFoundException {
		repo.findById(commentid).orElseThrow(() -> new CommentNotFoundException("comment with id " + commentid + " doesnot exists"));
		repo.deleteById(commentid);
	}

	@Override
	public List<CommentDto> getCommentListOfPostByPage(Long postId, Pageable pageable) throws PostNotFoundException {
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		List<Comment> comments = repo.findAllCommentsByPost(post, pageable).getContent();
		return comments.stream().map(comment -> mapper.toDto(comment)).collect(Collectors.toList());
	}

	@Override
	public boolean isPostCommentedByCurrentUser(Long postId) throws PostNotFoundException {
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Optional<Comment> comment = repo.findOneByCreatedByAndPost(user, post);
		return comment.isPresent();
	}

	@Override
	public List<CommentDto> getMostVotedCommentsOfaPost(Long postId, Pageable pageable) throws PostNotFoundException{
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		
		TypedSort<Comment> commentTypedSort = Sort.sort(Comment.class);
		Sort sortByUpVoteCount = commentTypedSort.by(Comment::getUpVoteCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sortByUpVoteCount);
		List<Comment> comments = repo.findAllCommentsByPost(post, firstSortedPage).getContent();
		return comments.stream().map(comment -> mapper.toDto(comment)).collect(Collectors.toList());
	}
}

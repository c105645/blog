package com.ness.postservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ness.postservice.dtos.CommentDto;
import com.ness.postservice.entities.Comment;
import com.ness.postservice.entities.Post;
import com.ness.postservice.exceptions.CommentNotFoundException;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.mapstructs.CommentMapper;
import com.ness.postservice.repositories.CommentRepository;
import com.ness.postservice.repositories.PostRepository;
import com.ness.postservice.services.CommentService;
import com.ness.postservice.services.VotesService;

import jakarta.transaction.Transactional;

@Service
public class CommentServiceImpl implements CommentService{

	private final CommentRepository repo;
	private final PostRepository postrepo;
	private final VotesService votesservice;


	private final CommentMapper mapper;

	public CommentServiceImpl(CommentRepository repo, VotesService votesservice, CommentMapper mapper, PostRepository postrepo) {
		this.repo = repo;
		this.mapper = mapper;
		this.postrepo=postrepo;
		this.votesservice=votesservice;
	}

	@Override
	@Transactional()
	public CommentDto getCommentById(long id) throws CommentNotFoundException {
		Comment comment = repo.findById(id)
				.orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " doesnot exists"));
		return mapper.toDto(comment);
	}

	@Override
	@Transactional()
	public CommentDto create(Long postid, CommentDto comment) throws PostNotFoundException {
		Post post = postrepo.findById(postid)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postid + " doesnot exists"));
		
		Comment commentEntity = mapper.toEntity(comment);
		
		commentEntity.setPost(post);
		Comment com =  repo.save(commentEntity);
		return mapper.toDto(com);
	}

	@Override
	@Transactional()
	public CommentDto update(Long postid, CommentDto comment) {
		Comment com =  repo.save(mapper.toEntity(comment));
		return mapper.toDto(com);
	}

	@Override
	@Transactional()
	public void delete(Long commentid) throws CommentNotFoundException {
		repo.findById(commentid).orElseThrow(() -> new CommentNotFoundException("comment with id " + commentid + " doesnot exists"));
		votesservice.deleteCommentVotes(commentid);
		repo.deleteById(commentid);
	}

	@Override
	@Transactional()
	public List<CommentDto> getCommentListOfPostByPage(Long postId, Pageable pageable) throws PostNotFoundException {
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		List<Comment> comments = repo.findAllCommentsByPost(post, pageable).getContent();
		return comments.stream().map(comment -> mapper.toDto(comment)).collect(Collectors.toList());
	}

	@Override
	@Transactional()
	public boolean isPostCommentedByCurrentUser(Long postId) throws PostNotFoundException {
		Post post = postrepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " doesnot exists"));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
				

		List<Comment> comments = repo.findAllByCreatedByAndPost(post, user);
		return comments.size() > 0;
	}

	@Override
	@Transactional()
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

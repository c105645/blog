package com.ness.postservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.entities.Post;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.mapstructs.PostMapper;
import com.ness.postservice.repositories.PostRepository;
import com.ness.postservice.services.PostService;

public class PostServiceImpl implements PostService {

	private final PostRepository repo;
	private final PostMapper postmapper;

	public PostServiceImpl(PostRepository repo, PostMapper postmapper) {
		this.repo = repo;
		this.postmapper = postmapper;
	}

	@Override
	public PostDto getPostById(long id) throws PostNotFoundException {
		Post post = repo.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + id + " doesnot exists"));
		return postmapper.toDto(post);
	}

	@Override
	public List<PostDto> getPostListByPage(Pageable pageable) {
		List<Post> posts = repo.findAll(pageable).getContent();
		return posts.stream().map(post -> postmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	public List<PostDto> getMostVotedPosts(Pageable pageable) {
		TypedSort<Post> postTypedSort = Sort.sort(Post.class);
		Sort sortByUpVoteCount = postTypedSort.by(Post::getUpVoteCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sortByUpVoteCount);
		List<Post> posts = repo.findAll(firstSortedPage).getContent();
		return posts.stream().map(post -> postmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	public List<PostDto> getMostCommentedPosts(Pageable pageable) {
		TypedSort<Post> postTypedSort = Sort.sort(Post.class);
		Sort sortByCommentsCount = postTypedSort.by(Post::getCommentCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sortByCommentsCount);
		List<Post> posts = repo.findAll(firstSortedPage).getContent();
		return posts.stream().map(post -> postmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	public List<PostDto> getPostListByCurrentUser() {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Post> posts = repo.findAllByCreatedBy(user);
		return posts.stream().map(post -> postmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	public List<PostDto> getMostVotedPostsByCategory(String category, Pageable pageable) {
		TypedSort<Post> postTypedSort = Sort.sort(Post.class);
		Sort sortByUpVoteCount = postTypedSort.by(Post::getUpVoteCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sortByUpVoteCount);
		List<Post> posts = repo.findByCategory(category, firstSortedPage).getContent();
		return posts.stream().map(post -> postmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	public List<PostDto> getMostCommentedPostsByCategory(String category, Pageable pageable) {
		TypedSort<Post> postTypedSort = Sort.sort(Post.class);
		Sort sortByCommentsCount = postTypedSort.by(Post::getCommentCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByCommentsCount);
		List<Post> posts = repo.findByCategory(category, firstSortedPage).getContent();
		return posts.stream().map(post -> postmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	public PostDto create(PostDto post) {
		Post postentity = repo.save(postmapper.toEntity(post));
		return postmapper.toDto(postentity);
	}

	@Override
	public PostDto update(PostDto post) {
		Post postentity = repo.save(postmapper.toEntity(post));
		return postmapper.toDto(postentity);
	}

	@Override
	public void delete(Long id) throws PostNotFoundException {
		repo.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id " + id + " doesnot exists"));

		repo.deleteById(id);
	}

	@Override
	public List<PostDto> getPostsCreatedByCurrentUser(Pageable pageable) {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Post> posts = repo.findByCreatedBy(user, pageable).getContent();
		return posts.stream().map(post -> postmapper.toDto(post)).collect(Collectors.toList());
	}

}

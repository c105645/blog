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


import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.entities.Post;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.mapstructs.PostMapper;
import com.ness.postservice.mapstructs.PostsListMapper;
import com.ness.postservice.repositories.PostRepository;
import com.ness.postservice.services.PostService;
import com.ness.postservice.services.VotesService;

import jakarta.transaction.Transactional;

@Service
public class PostServiceImpl implements PostService {

	private final PostRepository repo;
	private final VotesService votesservice;

	private final PostMapper postmapper;
	private final PostsListMapper postlistmapper;

	public PostServiceImpl(PostRepository repo, VotesService votesservice, PostMapper postmapper, PostsListMapper postlistmapper) {
		this.repo = repo;
		this.votesservice = votesservice;
		this.postmapper = postmapper;
		this.postlistmapper = postlistmapper;
	}

	@Override
	@Transactional()
	public PostDto getPostById(long id) throws PostNotFoundException {
		Post post = repo.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Post with id " + id + " doesnot exists"));
		return postmapper.toDto(post);
	}

	@Override
	@Transactional()
	public List<PostDto> getPostListByPage(Pageable pageable) {
		List<Post> posts = repo.findAll(pageable).getContent();
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	@Transactional()
	public List<PostDto> getMostVotedPosts(Pageable pageable) {
		TypedSort<Post> postTypedSort = Sort.sort(Post.class);
		Sort sortByUpVoteCount = postTypedSort.by(Post::getUpVoteCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sortByUpVoteCount);
		List<Post> posts = repo.findAll(firstSortedPage).getContent();
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	@Transactional()
	public List<PostDto> getMostCommentedPosts(Pageable pageable) {
		TypedSort<Post> postTypedSort = Sort.sort(Post.class);
		Sort sortByCommentsCount = postTypedSort.by(Post::getCommentCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sortByCommentsCount);
		List<Post> posts = repo.findAll(firstSortedPage).getContent();
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}


	@Override
	@Transactional()
	public List<PostDto> getMostVotedPostsByCategory(String category, Pageable pageable) {
		TypedSort<Post> postTypedSort = Sort.sort(Post.class);
		Sort sortByUpVoteCount = postTypedSort.by(Post::getUpVoteCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sortByUpVoteCount);
		List<Post> posts = repo.findAllByCategory(category, firstSortedPage).getContent();
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	@Transactional()
	public List<PostDto> getMostCommentedPostsByCategory(String category, Pageable pageable) {
		TypedSort<Post> postTypedSort = Sort.sort(Post.class);
		Sort sortByCommentsCount = postTypedSort.by(Post::getCommentCount).descending();
		Pageable firstSortedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByCommentsCount);
		List<Post> posts = repo.findAllByCategory(category, firstSortedPage).getContent();
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	@Transactional()
	public PostDto create(PostDto post) {
		Post postentity = repo.save(postmapper.toEntity(post));
		return postmapper.toDto(postentity);
	}

	@Override
	@Transactional()
	public PostDto update(PostDto post) {
		Post postentity = repo.save(postmapper.toEntity(post));
		return postmapper.toDto(postentity);
	}

	@Override
	@Transactional()
	public void delete(Long id) throws PostNotFoundException {
		repo.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id " + id + " doesnot exists"));
		votesservice.deletePostVotes(id);
		repo.deleteById(id);
	}

	@Override
	@Transactional()
	public List<PostDto> getPostsCreatedByCurrentUser(Pageable pageable) {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Post  postProbe = new Post();
		postProbe.setCreatedBy(user);
		
		Example<Post> postExample = Example.of(postProbe);

		List<Post> posts = repo.findAll(postExample, pageable).getContent();
		
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	@Transactional()
	public List<PostDto> getPostsByCategory(String categoery, Pageable pageable) {

		Pageable firstPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
		List<Post> posts = repo.findAllByCategory(categoery, firstPage).getContent();
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	@Transactional()
	public List<PostDto> getPostsByFollowing(List<String> following, Pageable pageable) {
		Pageable firstPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
		List<Post> posts = repo.findAllByFollowing(following, firstPage).getContent();
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}

	@Override
	public List<PostDto> getPostsByAuthor(String author, Pageable pageable) {
		Pageable firstPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
		List<Post> posts = repo.findAllByCreatedBy(author, firstPage).getContent();
		return posts.stream().map(post -> postlistmapper.toDto(post)).collect(Collectors.toList());
	}

}

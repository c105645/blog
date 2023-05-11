package com.ness.postservice.controllers;


import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ness.postservice.aspects.ToLog;
import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.dtos.ByAuthorRequest;
import com.ness.postservice.dtos.CategoryRequest;
import com.ness.postservice.dtos.FollowingRequest;
import com.ness.postservice.exceptions.PostAlreadyExistsException;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.services.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(PostserviceController.POST_API_ENDPOINT)
public class PostserviceController {

	public static final String POST_API_ENDPOINT = "/api/v1/post";
	
	public static final String COMMENT = "/comment";

	private final PostService service;


	public PostserviceController(PostService service) {
		this.service = service;
	}

	@ToLog
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new post")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "post created", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "409", description = "Post already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public PostDto createANewPost(@Valid @RequestBody PostDto post) throws PostAlreadyExistsException {
		return service.create(post);
	}
	
	@ToLog
	@PutMapping()
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "update a new post")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "post created", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public PostDto modifyPost(@Valid @RequestBody PostDto post) throws PostNotFoundException {
		return service.update(post);
	}
	
	
	@ToLog
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Create a new post")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "post created", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public void deletePost(@Valid @PathVariable Long id) throws PostNotFoundException {
		 service.delete(id);
	}
	
	
	@ToLog
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch post details")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public PostDto fetchPostById(@PathVariable Long id) throws PostNotFoundException {
		return service.getPostById(id);
	}
	
	@ToLog
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch post details")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "409", description = "Post already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchPosts(
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "2") int size
			){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getPostListByPage(pageable);
	}

	@ToLog
	@GetMapping("/mostvoted")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch most voted posts")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "409", description = "Post already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchMostVotedPosts(
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "2") int size
			){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getMostVotedPosts(pageable);
	}

	@ToLog
	@GetMapping("/mostcommented")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch most commented posts")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "409", description = "Post already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchMostCommentedPosts(
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "2") int size
			){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getMostCommentedPosts(pageable);
	}
	

	@ToLog
	@GetMapping("/byuser")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch posts created by current user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "409", description = "Post already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchPostsPageByCurrentUser(
			@RequestParam(name = "page", required = true, defaultValue = "0") int page,
	        @RequestParam(name = "size", required = true, defaultValue = "2") int size){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getPostsCreatedByCurrentUser(pageable);
	}
	
	@ToLog
	@GetMapping("/mostcommented/{category}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch most commented posts by category")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "409", description = "Post already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchMostCommentedPostsByCategory(
				@PathVariable String category,
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "2") int size
			){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getMostCommentedPostsByCategory(category, pageable);
	}
	
	
	
	@ToLog
	@GetMapping("/mostvoted/{category}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch most voted posts by category")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchMostVotedPostsByCategory(
				@PathVariable String category,
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "2") int size
			){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getMostVotedPostsByCategory(category, pageable);
	}

	@ToLog
	@PostMapping("/byfollowing")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch posts by following")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchPostsByFollowing(@RequestBody FollowingRequest following,
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "2") int size
			){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getPostsByFollowing(following, pageable);
	}
	
	@ToLog
	@PostMapping("/bycategoery")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch posts by category")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchPostsByCategory(@RequestBody CategoryRequest categoery,
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "2") int size
			){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getPostsByCategory(categoery, pageable);
	}
	
	@ToLog
	@PostMapping("/byauthor")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch posts by author")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch post details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<PostDto> fetchPostsByAuthor(@RequestBody ByAuthorRequest author,
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "2") int size
			){
		 Pageable pageable = PageRequest.of(page, size);
		return service.getPostsByAuthor(author, pageable);
	}
}

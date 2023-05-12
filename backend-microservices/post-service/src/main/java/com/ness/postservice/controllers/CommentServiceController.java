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
import com.ness.postservice.dtos.CommentDto;
import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.exceptions.CommentNotFoundException;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.services.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(CommentServiceController.COMMENT_API_ENDPOINT)
public class CommentServiceController {
	public static final String COMMENT_API_ENDPOINT = "/api/v1/post/{postid}/comment";

	private final CommentService service;


	public CommentServiceController(CommentService service) {
		this.service = service;
	}

	@ToLog
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "add a comment to a post")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "commet added", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "409", description = "comment already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public CommentDto createANewPost(@PathVariable Long postid, @Valid @RequestBody CommentDto comment) throws PostNotFoundException  {
		return service.create(postid, comment);
	}
	
	
	@ToLog
	@PutMapping()
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "update a comment on a post")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "commet updated", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "409", description = "comment already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public CommentDto updatePost(@Valid @PathVariable Long postid, @Valid @RequestBody CommentDto comment)  {
		return service.update(postid, comment);
	}
	
	@ToLog
	@DeleteMapping("/{commentId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "delete a comment on a post")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "commet deleted", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "comment doesnot exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public void deletePost(@Valid @PathVariable Long commentId) throws CommentNotFoundException  {
		 service.delete(commentId);
	}
	
	
	@ToLog
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch comments of a post")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch comments of a post", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<CommentDto> fetchPosts(@PathVariable Long postid,
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "25") int size
			) throws PostNotFoundException{
		 Pageable pageable = PageRequest.of(page, size);
		return service.getCommentListOfPostByPage(postid, pageable);
	}
	
	
	
	@ToLog
	@GetMapping("/iscommented")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "if user has commented the post")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "check if the current user has commented a post", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public boolean checkIfPostCommentedByCurrentUser(@PathVariable Long postid) throws PostNotFoundException{
		return service.isPostCommentedByCurrentUser(postid);
	}
	
	
	@ToLog
	@GetMapping("/mostvotedcomments")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch most voted comments of a post")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "fetch most voted comments of a post", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<CommentDto> fetchmostvotedPosts(@PathVariable Long postid,
				@RequestParam(name = "page", required = true, defaultValue = "0") int page,
		        @RequestParam(name = "size", required = true, defaultValue = "25") int size
			) throws PostNotFoundException{
		 Pageable pageable = PageRequest.of(page, size);
		return service.getMostVotedCommentsOfaPost(postid, pageable);
	}
	
}

package com.ness.postservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ness.postservice.aspects.ToLog;
import com.ness.postservice.dtos.CommentVotesDto;
import com.ness.postservice.dtos.PostDto;
import com.ness.postservice.dtos.PostVotesDto;
import com.ness.postservice.exceptions.CommentNotFoundException;
import com.ness.postservice.exceptions.PostNotFoundException;
import com.ness.postservice.exceptions.UserAlreadyVotedException;
import com.ness.postservice.services.VotesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping(VotesServiceController.POST_API_ENDPOINT)
public class VotesServiceController {
	
	public static final String POST_API_ENDPOINT = "/api/v1/post/{postid}";
	public static final String COMMENT_API_ENDPOINT = "/comment/{commentid}";


	private final VotesService service;

	public VotesServiceController(VotesService service) {
		this.service = service;
	}


	@ToLog
	@PostMapping("/upvote")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "upvote a post")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "upvote a post", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public PostVotesDto upvotepost(@PathVariable Long postid) throws PostNotFoundException, UserAlreadyVotedException{
		return service.upVotePostById(postid);
	}
	
	
	@ToLog
	@PostMapping("/downvote")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "downvote a post")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "upvote a post", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public PostVotesDto downVotePost(
				@PathVariable Long postid) throws PostNotFoundException, UserAlreadyVotedException{
		return service.downVotePostById(postid);
	}

	
	@ToLog
	@PostMapping(COMMENT_API_ENDPOINT + "/upvote")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "upvote a comment")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "upvote a comment", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "comment with given id doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public CommentVotesDto upvoteComment(
				@PathVariable Long commentid) throws CommentNotFoundException, UserAlreadyVotedException{
		return service.upVoteCommentById(commentid);
	}
	
	
	@ToLog
	@PostMapping(COMMENT_API_ENDPOINT + "/downvote")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "downvote a comment")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "downvote a post", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "comment with given id doesnot exists]", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public CommentVotesDto downVoteComment(
				@PathVariable Long postid) throws CommentNotFoundException, UserAlreadyVotedException{
		return service.downVoteCommentById(postid);
	}
	
	@ToLog
	@GetMapping("/hasvoted")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "if user has voted the post")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "check if the current user has voted a post", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Post doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public int checkIfPostVotedByCurrentUser(@PathVariable Long postid) throws PostNotFoundException{
		return service.isPostVotedByCurrentUser(postid);
	}

	@ToLog
	@GetMapping(COMMENT_API_ENDPOINT + "/hasvoted")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "is comment voted by the current user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "check if the current user has voted a comment", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Comment doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public int checkIfCommentVotedByCurrentUser(@PathVariable Long commentid) throws CommentNotFoundException{
		return service.isCommentVotedByCurrentUser(commentid);
	}
}
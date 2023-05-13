package com.ness.userprofileservice.controllers;

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

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import com.ness.userprofileservice.aspects.ToLog;
import com.ness.userprofileservice.dtos.CategoryDto;
import com.ness.userprofileservice.dtos.FollowRequest;
import com.ness.userprofileservice.dtos.SearchByCriteria;
import com.ness.userprofileservice.dtos.SearchByCriteriaResults;
import com.ness.userprofileservice.dtos.UnFollowRequest;
import com.ness.userprofileservice.dtos.UserProfileDto;
import com.ness.userprofileservice.entities.Category;
import com.ness.userprofileservice.exceptions.CategoryAlreadyExistsException;
import com.ness.userprofileservice.exceptions.CategoryNotFoundException;
import com.ness.userprofileservice.exceptions.OperationNotAllowedException;
import com.ness.userprofileservice.exceptions.UserAlreadyExistsException;
import com.ness.userprofileservice.exceptions.UserNotFoundException;
import com.ness.userprofileservice.services.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(UserProfileController.USER_API_ENDPOINT)
@SecurityRequirement(name = "api-security-scheme")
public class UserProfileController {

	public static final String USER_API_ENDPOINT = "/api/v1/userprofile";
	public static final String SIGNUP = "/signup";
	public static final String FOLLOW = "/follow";
	public static final String UNFOLLOW = "/unfollow";
	public static final String FOLLOWERS = "/followers";
	public static final String FOLLOWING = "/following";
	public static final String CATEGORIES = "/categoery";
	public static final String CATEGOERY = "/categoery/{id}";
	public static final String FOLLOWCATEGOERY = "/categoery/{id}/follow";
	public static final String UNFOLLOWCATEGOERY = "/categoery/{id}/unfollow";

	private final UserProfileService service;

	public UserProfileController(UserProfileService service) {
		this.service = service;
	}

	@ToLog
	@PostMapping(SIGNUP)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add a new profile")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "profile added", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "409", description = "User profile already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public UserProfileDto registerUser(@Valid @RequestBody UserProfileDto user) throws UserAlreadyExistsException {
		return service.registerUser(user);
	}

	@ToLog
	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{userId}")
	@Operation(summary = "Update an existing profile")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "profile Updated", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "User profile dont exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public UserProfileDto updateUser(@Valid @RequestBody UserProfileDto user, @PathVariable Long userId)
			throws UserNotFoundException, OperationNotAllowedException {
		return service.updateUser(userId, user);
	}

	@ToLog
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Delete a profile")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "profile deleted", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "User profile does not exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public void updateUser(@PathVariable Long userId) throws UserNotFoundException {
		service.deleteUser(userId);
	}

	@ToLog
	@GetMapping("/verifyemail/{email}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "verify if email is already used")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "profile fetched", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "User profile does not exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public boolean VerifyIfEmailAlreadyExists(@PathVariable @Valid String email) throws UserNotFoundException {
		return service.VerifyIfEmailAlreadyExists(email);
	}

	@ToLog
	@GetMapping("/verifyuser/{username}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "verify if user name is already used.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "profile fetched", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "User profile does not exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public boolean VerifyIfUsernameAlreadyExists(@PathVariable @Valid String username) throws UserNotFoundException {
		return service.VerifyIfUsernameAlreadyExists(username);
	}

	@ToLog
	@GetMapping("/{username}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch a profile based on username")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "profile fetched", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "User profile does not exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public UserProfileDto fetchByUsername(@PathVariable @Valid String username) throws UserNotFoundException {
		return service.fetchByUsername(username);
	}

	@ToLog
	@GetMapping("")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch a profile based on username")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "profile fetched", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "User profile does not exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<UserProfileDto> fetchAllUsers() throws UserNotFoundException {
		return service.fetchUsers();
	}

	@ToLog
	@PostMapping(FOLLOW)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "follow an author")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "follower added", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public void followAuthor(@Valid @RequestBody FollowRequest request) throws UserNotFoundException {
		System.out.println("Follow Request:" + request.userId() + " : " + request.toFollowId());
		service.addFollower(request.userId(), request.toFollowId());
	}

	@ToLog
	@PostMapping(UNFOLLOW)
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "unFollow an author")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "follower removed", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public void unFollowAuthor(@Valid @RequestBody UnFollowRequest request) throws UserNotFoundException {
		service.unfollow(request.userId(), request.toUnFollowId());
	}

	@ToLog
	@GetMapping(FOLLOWERS + "/{Id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch all followers of a user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Followers fetched", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "User profile does not exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<UserProfileDto> fetchAllFollowers(@PathVariable @Valid Long Id) throws UserNotFoundException {
		return service.getFollowers(Id);
	}

	@ToLog
	@GetMapping(FOLLOWING + "/{Id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch all users following an Author")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Followers fetched", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "User profile does not exist", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<UserProfileDto> fetchAllFollowing(@PathVariable @Valid Long Id) throws UserNotFoundException {
		return service.getFollowing(Id);
	}

	@ToLog
	@GetMapping(CATEGORIES)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "fetch all categories")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "categories fetched", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "categories dont exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<CategoryDto> fetchCategories() {
		return service.fetchAllCategories();
	}

	@ToLog
	@PostMapping(CATEGORIES)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add a new categoery")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "categoery added", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "409", description = "categoery already exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public CategoryDto addCategoery(@Valid @RequestBody CategoryDto category) throws CategoryAlreadyExistsException {
		return service.addNewCategoery(category);
	}

	@ToLog
	@PutMapping(CATEGORIES)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "update a categoery")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "categoery updated", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "categoery doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public CategoryDto updateCategoery(@Valid @RequestBody CategoryDto category) throws CategoryNotFoundException {
		return service.updateCategoery(category.id(), category);
	}

	@ToLog
	@DeleteMapping(CATEGOERY)
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "delete a categoery")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "categoery deleted", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "404", description = "categoery doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public void deleteCategoery(@PathVariable Long id) throws CategoryNotFoundException {
		service.deleteCategoery(id);
	}

	@ToLog
	@PostMapping(FOLLOWCATEGOERY)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "follow a Topic")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Topic added to the user", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public void followTopic(@Valid @PathVariable Long id) throws CategoryNotFoundException, UserNotFoundException {
		service.addTopic(id);
	}

	@ToLog
	@PostMapping(UNFOLLOWCATEGOERY)
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "un follow a Topic")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Topic removed from the user", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public void unFollowTopic(@Valid @PathVariable Long id) throws CategoryNotFoundException, UserNotFoundException {
		service.removeTopic(id);
	}

	@ToLog
	@PostMapping("/authors/following")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch following list of a list of authors")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "fetch authors List of following of following list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "404", description = "authors doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public List<UserProfileDto> fetchAuthorsList(@RequestBody List<String> req,
			@RequestParam(name = "page", required = true, defaultValue = "0") int page,
			@RequestParam(name = "size", required = true, defaultValue = "25") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return service.getAuthorsFollowedBy(req, pageable);
	}

	@ToLog
	@PostMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "fetch following list of a list of authors")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "fetch authors List of following of following list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "404", description = "authors doesnot exists", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "401", description = "Un-Authorized user", content = {
					@Content(schema = @Schema(hidden = true)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(schema = @Schema(hidden = true)) }) })
	public SearchByCriteriaResults search(@RequestBody SearchByCriteria req,
			@RequestParam(name = "page", required = true, defaultValue = "0") int page,
			@RequestParam(name = "size", required = true, defaultValue = "25") int size) {
		Pageable pageable = PageRequest.of(page, size);
		if (req.searchBy().equals("author")) {
			return service.getMatchingAuthor(req.searchString(), pageable);
		} else if (req.searchBy().equals("categoery")) {
			return service.getMatchingCategories(req.searchString(), pageable);
		} else
			return null;
	}
}

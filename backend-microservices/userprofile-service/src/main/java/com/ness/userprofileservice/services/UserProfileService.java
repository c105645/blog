package com.ness.userprofileservice.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ness.userprofileservice.dtos.CategoryDto;
import com.ness.userprofileservice.dtos.UserProfileDto;
import com.ness.userprofileservice.exceptions.CategoryAlreadyExistsException;
import com.ness.userprofileservice.exceptions.CategoryNotFoundException;
import com.ness.userprofileservice.exceptions.OperationNotAllowedException;
import com.ness.userprofileservice.exceptions.UserAlreadyExistsException;
import com.ness.userprofileservice.exceptions.UserNotFoundException;
import jakarta.validation.Valid;

@Service
public interface UserProfileService {



	UserProfileDto registerUser(UserProfileDto user) throws UserAlreadyExistsException;
	
	CategoryDto addNewCategoery(CategoryDto categoery) throws CategoryAlreadyExistsException;
	CategoryDto updateCategoery(Long id, CategoryDto categoery) throws CategoryNotFoundException;
	void deleteCategoery(Long id) throws CategoryNotFoundException;




	UserProfileDto updateUser(Long userId,@Valid UserProfileDto user) throws UserNotFoundException, OperationNotAllowedException;

  void deleteUser(Long userId) throws UserNotFoundException;

  UserProfileDto getUserById(Long userId) throws UserNotFoundException;

  boolean VerifyIfEmailAlreadyExists(String email);
  
  boolean VerifyIfUsernameAlreadyExists(String username);

  UserProfileDto fetchByUsername(String username) throws UserNotFoundException;

  List<UserProfileDto> getFollowing(Long userId) throws UserNotFoundException;
	
  List<UserProfileDto> getFollowers(Long userId) throws UserNotFoundException;
	
  void unfollow(Long userId, Long toUnFollowId) throws UserNotFoundException;
	
  void addFollower(Long userId, Long toFollowId) throws UserNotFoundException;
  
  void addTopic(Long topicId) throws CategoryNotFoundException, UserNotFoundException;

void removeTopic(Long topicId) throws CategoryNotFoundException, UserNotFoundException;

}


	

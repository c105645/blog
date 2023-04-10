package com.ness.userprofileservice.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ness.userprofileservice.dtos.MyUserDetails;
import com.ness.userprofileservice.entities.UserProfileEntity;
import com.ness.userprofileservice.repositories.UserProfileRepository;

import jakarta.transaction.Transactional;




@Service
public class MyUserDetailsService implements UserDetailsService {
	
	private final UserProfileRepository userRepo;

	public MyUserDetailsService(UserProfileRepository userRepo) {
		this.userRepo = userRepo;
	}

  	@Override
	@Transactional()
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 UserProfileEntity savedUser = userRepo.findUserProfileByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with Id" + username + "dont exist"));
		 
		 Set<SimpleGrantedAuthority> roles = new HashSet<SimpleGrantedAuthority>();	
		 roles.add(new SimpleGrantedAuthority("ROLE_user"));
		 if(savedUser.getIsAdmin()) roles.add(new SimpleGrantedAuthority("ROLE_admin"));
			
		 User user = new User(savedUser.getUsername(), savedUser.getPassword(), roles);
		 MyUserDetails desired=new MyUserDetails(user);
	        System.out.println("desired : " + desired.getUsername() + " " + desired.getPassword() + " " + desired.getAuthorities() );
	        return desired;
	}
	
	
}
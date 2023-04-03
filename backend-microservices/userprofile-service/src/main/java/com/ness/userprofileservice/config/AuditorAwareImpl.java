package com.ness.userprofileservice.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import com.ness.userprofileservice.dtos.MyUserDetails;


public class AuditorAwareImpl implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
   	    return Optional.of(((MyUserDetails) authentication.getPrincipal()).getUser());
	}

}

package com.mongoddemo.demo.service;

import com.mongoddemo.demo.config.SpringUser;
import com.mongoddemo.demo.exception.NotFoundException;
import com.mongoddemo.demo.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SpringUserService implements UserDetailsService {

	@Autowired
	private AppUserService appUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			AppUser user = appUserService.getUserByEmail(username);
			return new SpringUser(user);
		} catch (NotFoundException e) {
			throw new UsernameNotFoundException("Username is wrong.");
		}
	}

}
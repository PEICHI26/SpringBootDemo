package com.mongoddemo.demo.service;

import com.mongoddemo.demo.util.UserDetailsImpl;
import com.mongoddemo.demo.exception.NotFoundException;
import com.mongoddemo.demo.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//它會在按下「Sign in」按鈕後被呼叫。
@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserService appUserService;

	// UserDetails 介面的物件作為使用者詳情的載體。
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			AppUser user = appUserService.getUserByEmail(username);
			//UserDetailsImpl implement UserDetails
			return new UserDetailsImpl(user);
		} catch (NotFoundException e) {
			throw new UsernameNotFoundException("Username is wrong.");
		}
	}

}
package com.mongoddemo.demo.unit;

import com.mongoddemo.demo.util.UserDetailsImpl;
import com.mongoddemo.demo.entity.AppUser;
import com.mongoddemo.demo.exception.NotFoundException;
import com.mongoddemo.demo.service.AppUserService;
import com.mongoddemo.demo.service.UserDetailServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	//Inject 的
	//	@Mock
	//	private AppUserService appUserService;
	//
	//	//要測試的
	//	@InjectMocks
	//	private UserDetailServiceImpl userDetailServiceImpl;
	//
	//	@Test
	//	public void testLoadUser() {
	//		AppUser appUser = new AppUser();
	//		String email = "vin@gmail.com";
	//		appUser.setId("123");
	//		appUser.setEmailAddress(email);
	//		appUser.setName("Vin");
	//		when(appUserService.getUserByEmail(email))
	//			.thenReturn(appUser);
	//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailServiceImpl.loadUserByUsername(email);
	//		Assertions.assertEquals(userDetailsImpl.getId(), appUser.getId());
	//		Assertions.assertEquals(appUser.getName(), userDetailsImpl.getName());
	//	}
	//
	//	@Test(expected = UsernameNotFoundException.class)
	//	public void testUserLoadException() {
	//		when(userDetailServiceImpl.loadUserByUsername(anyString()))
	//			.thenThrow(new NotFoundException(anyString()));
	//		userDetailServiceImpl.loadUserByUsername("vincent@gmail.com");
	//	}

}

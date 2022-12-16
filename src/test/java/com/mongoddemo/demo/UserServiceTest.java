package com.mongoddemo.demo;

import com.mongoddemo.demo.config.SpringUser;
import com.mongoddemo.demo.entity.AppUser;
import com.mongoddemo.demo.exception.NotFoundException;
import com.mongoddemo.demo.service.AppUserService;
import com.mongoddemo.demo.service.SpringUserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	//Inject 的
	@Mock
	private AppUserService appUserService;

	//要測試的
	@InjectMocks
	private SpringUserService springUserService;

	@Test
	public void testLoadUser() {
		AppUser appUser = new AppUser();
		String email = "vin@gmail.com";
		appUser.setId("123");
		appUser.setEmailAddress(email);
		appUser.setName("Vin");
		when(appUserService.getUserByEmail(email))
			.thenReturn(appUser);
		SpringUser springUser = (SpringUser) springUserService.loadUserByUsername(email);
		Assertions.assertEquals(springUser.getId(), appUser.getId());
		Assertions.assertEquals(appUser.getName(), springUser.getName());
	}

	@Test(expected = UsernameNotFoundException.class)
	public void testUserLoadException() {
		when(springUserService.loadUserByUsername(anyString()))
			.thenThrow(new NotFoundException(anyString()));
		springUserService.loadUserByUsername("vincent@gmail.com");
	}

}

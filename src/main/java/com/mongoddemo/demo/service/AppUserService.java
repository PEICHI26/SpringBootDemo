package com.mongoddemo.demo.service;

import com.mongoddemo.demo.aop.ActionType;
import com.mongoddemo.demo.aop.EntityType;
import com.mongoddemo.demo.aop.SendEmail;
import com.mongoddemo.demo.converter.AppUserConverter;
import com.mongoddemo.demo.exception.NotFoundException;
import com.mongoddemo.demo.exception.UnprocessableEntityException;
import com.mongoddemo.demo.entity.AppUser;
import com.mongoddemo.demo.model.request.AppUserRequest;
import com.mongoddemo.demo.model.response.AppUserResponse;
import com.mongoddemo.demo.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AppUserService {
	@Autowired
	AppUserRepository repository;

	private BCryptPasswordEncoder passwordEncoder;

	public AppUserService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	@SendEmail(action = ActionType.CREATE, entity = EntityType.USER)
	public AppUserResponse createUser(AppUserRequest request) {
		Optional<AppUser> existingUser = repository.findByEmailAddress(request.getEmailAddress());
		if (existingUser.isPresent()) {
			throw new UnprocessableEntityException("This email address has been used.");
		}
		AppUser appUser = new AppUser();
		AppUserResponse appUserResponse = new AppUserResponse();
		BeanUtils.copyProperties(request, appUser);
		String encode = passwordEncoder.encode(appUser.getPassword());
		appUser.setPassword(encode);
		AppUser savedUser = repository.save(appUser);
		BeanUtils.copyProperties(savedUser, appUserResponse);
		return appUserResponse;
	}

	public AppUser getUserByEmail(String email) {
		return repository.findByEmailAddress(email)
			.orElseThrow(() -> new NotFoundException("Can't find user."));
	}

	public List<AppUserResponse> getUserResponses() {
		List<AppUser> users = repository.findAll();
		return AppUserConverter.toAppUserResponses(users);
	}

	public AppUserResponse getUserResponseById(String id) {
		AppUser user = repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Can't find user."));

		return AppUserConverter.toAppUserResponse(user);
	}

}

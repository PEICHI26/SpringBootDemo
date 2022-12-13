package com.mongoddemo.demo.controller;

import com.mongoddemo.demo.model.request.AppUserRequest;
import com.mongoddemo.demo.model.response.AppUserResponse;
import com.mongoddemo.demo.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AppUserController {

	private final AppUserService service;

	@PostMapping
	public ResponseEntity<AppUserResponse> createUser(@Valid @RequestBody AppUserRequest request) {
		AppUserResponse user = service.createUser(request);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<AppUserResponse>> getUsers() {
		List<AppUserResponse> users = service.getUserResponses();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") String id) {
		AppUserResponse user = service.getUserResponseById(id);
		return ResponseEntity.ok(user);
	}

}

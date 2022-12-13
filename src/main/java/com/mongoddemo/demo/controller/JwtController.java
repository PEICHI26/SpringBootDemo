package com.mongoddemo.demo.controller;

import com.mongoddemo.demo.service.JWTService;
import com.mongoddemo.demo.model.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class JwtController {
	@Autowired
	JWTService jwtService;

	@PostMapping
	public ResponseEntity<Map<String, String>> issueToken(@Valid @RequestBody AuthRequest authRequest) {
		String token = jwtService.generateToken(authRequest);
		Map<String, String> tokenMap = Collections.singletonMap("token", token);
		return ResponseEntity.ok(tokenMap);
	}

	@PostMapping("/parse")
	public ResponseEntity<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
		Map<String, Object> tokenMap = jwtService.parseToken(request.get("token"));
		return ResponseEntity.ok(tokenMap);
	}

}

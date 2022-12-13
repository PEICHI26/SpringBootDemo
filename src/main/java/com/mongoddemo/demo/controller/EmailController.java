package com.mongoddemo.demo.controller;

import com.mongoddemo.demo.config.SpringUser;
import com.mongoddemo.demo.model.request.EmailDetails;
import com.mongoddemo.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Class
public class EmailController {

	@Autowired
	private EmailService emailService;

	// Sending a simple Email
	@PostMapping("/sendMail")
	public String
	sendMail(@AuthenticationPrincipal SpringUser springUser, @RequestBody EmailDetails details) {
		String status
			= emailService.sendSimpleMail(springUser.getUsername(), details);

		return status;
	}

}
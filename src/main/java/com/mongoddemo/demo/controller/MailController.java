package com.mongoddemo.demo.controller;

import com.mongoddemo.demo.model.request.SendMailRequest;
import com.mongoddemo.demo.service.MailService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/mail", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailController {

	@Autowired
	private MailService mailService;

	@PostMapping
	public ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request) {
		mailService.sendMail(request);
		System.out.println("Call send mail api");
		return ResponseEntity.noContent().build();
	}

}
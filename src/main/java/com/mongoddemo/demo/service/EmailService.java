package com.mongoddemo.demo.service;

import com.mongoddemo.demo.model.request.EmailDetails;

public interface EmailService {
	String sendSimpleMail(String sender, EmailDetails details);
}

package com.mongoddemo.demo.service;

import com.mongoddemo.demo.model.request.SendMailRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class MailService {

	private final JavaMailSenderImpl mailSender;
	private final long tag;
	private final List<String> mailMessages;

	public MailService(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
		this.tag = System.currentTimeMillis();
		this.mailMessages = new ArrayList<>();
	}

	public void sendMail(String subject, String content, List<String> receivers) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailSender.getUsername());
		message.setTo(receivers.toArray(new String[0]));
		message.setSubject(subject);
		message.setText(content);

		try {
			mailSender.send(message);
			mailMessages.add(content);
			printMessages();
		} catch (MailAuthenticationException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	private void printMessages() {
		System.out.println("----------");
		mailMessages.forEach(System.out::println);
	}

	@PreDestroy
	public void preDestroy() {
		System.out.println("##########");
		System.out.printf("Spring Boot is about to destroy Mail Service %d.\n\n", tag);
	}
}
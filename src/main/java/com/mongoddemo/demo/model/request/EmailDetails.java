package com.mongoddemo.demo.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;
}

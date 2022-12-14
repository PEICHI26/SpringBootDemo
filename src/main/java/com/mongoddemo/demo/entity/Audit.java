package com.mongoddemo.demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
public class Audit {
	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

	@CreatedBy
	private String createdByUser;

	@LastModifiedBy
	private String modifiedByUser;
}

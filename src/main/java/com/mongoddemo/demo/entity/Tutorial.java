package com.mongoddemo.demo.entity;

import com.mongoddemo.demo.model.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tutorials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tutorial extends AuditModel {
	@Id
	private String id;
	private String title;
	private String description;
	private boolean published;

	public Tutorial(String title, String description, boolean published) {
		this.title = title;
		this.description = description;
		this.published = published;
	}
}
package com.mongoddemo.demo.entity;

import com.mongoddemo.demo.model.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product extends AuditModel {
	@Id
	private String id;
	private String name;
	private int price;

}
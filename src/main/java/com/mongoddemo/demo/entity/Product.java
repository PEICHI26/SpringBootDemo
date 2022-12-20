package com.mongoddemo.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product extends Audit {
	@Id
	private String id;
	@NotEmpty(message = "Product name is undefined.")
	private String name;

	//int 預設值是 0 ,Integer 預設值是null
	@Min(value = 0, message = "Price should be greater or equal to 0.")
	private int price;

}

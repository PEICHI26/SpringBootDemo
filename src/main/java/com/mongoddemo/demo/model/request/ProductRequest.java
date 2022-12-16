package com.mongoddemo.demo.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductRequest {

	@NotEmpty(message = "Product name is undefined.")
	private String name;

	@NotNull
	@Min(value = 0, message = "Price should be greater or equal to 0.")
	private Integer price;

}
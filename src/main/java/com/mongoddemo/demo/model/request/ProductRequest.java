package com.mongoddemo.demo.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductRequest {

	//NOT_EMPTY 空集合或空字串
	@NotEmpty(message = "Product name is undefined.")
	@JsonProperty("productName")
	private String name;

	@NotNull
	@Min(value = 0, message = "Price should be greater or equal to 0.")
	private Integer price;

	@JsonIgnore
	private String isbn;

}
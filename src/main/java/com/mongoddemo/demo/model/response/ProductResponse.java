package com.mongoddemo.demo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
	private String id;
	@JsonProperty("productName")
	private String name;
	private int price;

}
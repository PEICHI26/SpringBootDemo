package com.mongoddemo.demo.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductQueryParameter {
	private String keyword;
	private Integer priceFrom;
	private Integer priceTo;
	private String orderBy;
	private String sortRule;
}

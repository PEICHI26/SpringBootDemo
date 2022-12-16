package com.mongoddemo.demo.converter;

import com.mongoddemo.demo.entity.Product;
import com.mongoddemo.demo.model.request.ProductRequest;
import com.mongoddemo.demo.model.response.ProductResponse;

public class ProductConverter {

	private ProductConverter() {

	}

	public static ProductResponse toProductResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setPrice(product.getPrice());

		return response;
	}

	public static Product toProduct(ProductRequest request) {
		Product product = new Product();
		product.setName(request.getName());
		product.setPrice(request.getPrice());

		return product;
	}
}
package com.mongoddemo.demo.controller;

import com.mongoddemo.demo.model.request.ProductQueryParameter;
import com.mongoddemo.demo.model.request.ProductRequest;
import com.mongoddemo.demo.model.response.ProductResponse;
import com.mongoddemo.demo.service.ProductService;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") String id) {
		ProductResponse product = productService.getProductResponse(id);
		return ResponseEntity.ok(product);
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getProducts(@ModelAttribute ProductQueryParameter param) {
		List<ProductResponse> products = productService.getProductResponses(param);
		return ResponseEntity.ok(products);
	}

	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		ProductResponse product = productService.createProduct(request);
		return ResponseEntity.ok(product);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> replaceProduct(@PathVariable("id") String id,
		@Valid @RequestBody ProductRequest request) {
		ProductResponse product = productService.replaceProduct(id, request);
		return ResponseEntity.ok(product);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteProduct(@PathVariable("id") String id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

}
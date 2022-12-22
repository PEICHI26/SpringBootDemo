package com.mongoddemo.demo.controller;

import com.mongoddemo.demo.entity.Product;
import com.mongoddemo.demo.model.request.ProductRequest;
import com.mongoddemo.demo.model.response.ProductResponse;
import com.mongoddemo.demo.model.response.RateResponse;
import com.mongoddemo.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

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
	public ResponseEntity<Page<ProductResponse>> getProducts(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer priceFrom,
		@RequestParam(required = false) Integer priceTo,
		@RequestParam(value = "page", required = false, defaultValue = "") Integer page,
		@RequestParam(value = "size", required = false, defaultValue = "") Integer size, @RequestParam(value = "sort", required = false) String sort, Pageable pageable) {
		Page<ProductResponse> productResponses = productService.getProductResponses(keyword, priceFrom, priceTo, pageable);
		return ResponseEntity.ok(productResponses);
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

	@GetMapping("/exchange/rate")
	public ResponseEntity<RateResponse> getExchangeRate(@RequestParam(required = true) String currencyFrom, @RequestParam(required = true) String currencyTo) {
		RateResponse rates = productService.getRates(currencyFrom, currencyTo);
		return ResponseEntity.ok(rates);
	}

}
package com.mongoddemo.demo.integration;

import com.mongoddemo.demo.entity.Product;
import com.mongoddemo.demo.model.request.ProductRequest;
import com.mongoddemo.demo.model.response.ProductResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
public class restTemplateTest extends BaseTest {

	@LocalServerPort
	private int port;

	private String domain;
	private RestTemplate restTemplate;

	@Before
	public void init() {
		domain = "http://localhost:" + port;
		restTemplate = new RestTemplate();
	}

	@Test
	public void testGetProduct() throws Exception {
		login();
		Product palle = createProduct("Palle", 35);

		String url = domain + "/products/" + palle.getId();
		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<ProductResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, ProductResponse.class);
		ProductResponse response = responseEntity.getBody();

		assert response != null;
		Assertions.assertEquals(palle.getId(), response.getId());
		Assertions.assertEquals(palle.getName(), response.getName());
		Assertions.assertEquals(palle.getPrice(), response.getPrice());
	}

	@Test
	public void testGetProducts() throws Exception {
		login();

		Product bed = createProduct("bed", 67);
		Product tower = createProduct("tower man", 78);
		Product door = createProduct("door", 34);
		Product hat = createProduct("hat man", 12);

		HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
		String url = domain + "/products?keyword={name}&orderBy={orderField}&sortRule={sortField}";
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("name", "man");
		paramMap.put("orderField", "price");
		paramMap.put("sortField", "asc");

		ResponseEntity<List<ProductResponse>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<ProductResponse>>() {
		}, paramMap);
		List<ProductResponse> productResponseList = responseEntity.getBody();
		assert productResponseList != null;

		Assert.assertEquals(2, productResponseList.size());
		Assert.assertEquals(productResponseList.get(0).getId(), hat.getId());
		Assert.assertEquals(productResponseList.get(1).getId(), tower.getId());

	}

	@Test
	public void testCreateProduct() throws Exception {
		login();

		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("Robot");
		productRequest.setPrice(56);

		HttpEntity<ProductRequest> productRequestHttpEntity = new HttpEntity<>(productRequest, httpHeaders);
		String url = domain + "/products";
		ResponseEntity<ProductResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, productRequestHttpEntity, ProductResponse.class);
		ProductResponse productResponse = responseEntity.getBody();

		assert productResponse != null;
		Assert.assertEquals(productRequest.getName(), productResponse.getName());
		Assert.assertEquals(productRequest.getPrice().intValue(), productResponse.getPrice());

	}

	private Product createProduct(String name, int price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		productRepository.insert(product);
		return product;
	}
}

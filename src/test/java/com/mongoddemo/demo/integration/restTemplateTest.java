package com.mongoddemo.demo.integration;

import com.mongoddemo.demo.entity.AppUser;
import com.mongoddemo.demo.entity.Product;
import com.mongoddemo.demo.model.UserAuthority;
import com.mongoddemo.demo.model.response.ProductResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

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
		Product palle = createProduct("Palle", 35);
		String url = domain + "/products/" + palle.getId();
		ProductResponse response = restTemplate.getForObject(url, ProductResponse.class);
		assert response != null;
		Assertions.assertEquals(palle.getId(), response.getId());
		Assertions.assertEquals(palle.getName(), response.getName());
		Assertions.assertEquals(palle.getPrice(), response.getPrice());
	}

	private Product createProduct(String name, int price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		productRepository.insert(product);
		return product;
	}
}

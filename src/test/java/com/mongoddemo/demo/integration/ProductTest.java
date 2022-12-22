package com.mongoddemo.demo.integration;

import com.mongoddemo.demo.entity.AppUser;
import com.mongoddemo.demo.entity.Product;
import com.mongoddemo.demo.model.UserAuthority;
import com.mongoddemo.demo.model.request.ProductRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class ProductTest extends BaseTest {

	@Test
	public void teatCreateProduct() throws Exception {

		//create new user and login(put token in the header)
		login();

		//product request
		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("Water");
		productRequest.setPrice(76);

		//call api create product
		MvcResult mvcResult = mockMvc.perform(post("/products")
				.headers(httpHeaders)
				.content(mapper.writeValueAsString(productRequest)))
			.andReturn();

		//transfer string to json object
		String contentAsString = mvcResult.getResponse().getContentAsString();
		JSONObject response = new JSONObject(contentAsString);

		//assertion
		Assertions.assertEquals(response.getString("productName"), productRequest.getName());
		Assertions.assertEquals(response.getInt("price"), productRequest.getPrice());

	}

	@Test
	public void testMyGetProduct() throws Exception {
		login();
		Product tea = createProduct("Tea", 34);
		MvcResult mvcResult = mockMvc.perform(get("/products/" + tea.getId())
				.headers(httpHeaders))
			.andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		JSONObject response = new JSONObject(contentAsString);

		Assertions.assertEquals(response.getString("id"), tea.getId());
		Assertions.assertEquals(response.getString("productName"), tea.getName());
		Assertions.assertEquals(response.getInt("price"), tea.getPrice());

	}

	@Test
	public void testUpdate() throws Exception {
		login();

		Product tea = createProduct("Tea", 34);

		ProductRequest productRequest = new ProductRequest();
		productRequest.setPrice(66);
		productRequest.setName("Hey");

		MvcResult mvcResult = mockMvc.perform(put("/products/" + tea.getId())
				.headers(httpHeaders)
				//object轉成String去傳遞資料
				.content(mapper.writeValueAsString(productRequest)))
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		JSONObject response = new JSONObject(contentAsString);

		Assertions.assertEquals(response.getString("id"), tea.getId());
		Assertions.assertEquals(response.getString("productName"), productRequest.getName());
		Assertions.assertEquals(response.getInt("price"), productRequest.getPrice());

	}

	@Test(expected = RuntimeException.class)
	public void testDeleteProduct() throws Exception {
		login();
		Product tea = createProduct("Tea", 34);
		mockMvc.perform(delete("/products/" + tea.getId())
				.headers(httpHeaders))
			.andExpect(status().isNoContent());

		productRepository.findById(tea.getId())
			.orElseThrow(RuntimeException::new);
	}

	@Test
	public void testGetProductsByCondition() throws Exception {
		Product tea = createProduct("Tea", 34);
		Product coo = createProduct("Coo", 56);
		Product p = createProduct("management2", 45);
		Product p2 = createProduct("management7", 85);

		productRepository.insert(Arrays.asList(tea, coo, p, p2));

		MvcResult mvcResult = mockMvc.perform(get("/products")
				.headers(httpHeaders)
				.param("keyword", "man")
				.param("orderBy", "price")
				.param("sortRule", "asc"))
			.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		//回應主體的 JSON 字串
		String contentAsString = response.getContentAsString();
		//轉成JsonArray
		JSONArray jsonArray = new JSONArray(contentAsString);
		List<String> productIds = new ArrayList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			productIds.add(id);
		}
		Assert.assertEquals(productIds.size(), 2);
		Assert.assertEquals(productIds.get(0), p.getId());
		Assert.assertEquals(productIds.get(1), p2.getId());

		Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
		Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeader(HttpHeaders.CONTENT_TYPE));
	}

	@Test
	public void test400BadRequest() throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", "hEPP")
			.put("price", -2);
		mockMvc.perform(post("/products")
				.headers(httpHeaders)
				.content(jsonObject.toString()))
			.andExpect(status().isBadRequest());
	}

	private Product createProduct(String name, int price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		return productRepository.insert(product);
	}

}

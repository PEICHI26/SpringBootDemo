package com.mongoddemo.demo;

import com.mongoddemo.demo.entity.Product;
import com.mongoddemo.demo.model.request.ProductRequest;
import com.mongoddemo.demo.model.response.ProductResponse;
import com.mongoddemo.demo.repository.ProductRepository;
import com.mongoddemo.demo.service.ProductService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceTest {

	//連不上真實的資料庫，但在測試方法邏輯時，仍可透過模擬物件假設它有正常運作。
	@Test
	public void testGetProduct() {
		String productId = "123";
		Product testProduct = new Product();
		testProduct.setId(productId);
		testProduct.setName("Snack");
		testProduct.setPrice(50);
		//使用 mock 方法，傳入類別或介面的 class，即可得到一個模擬物件。
		ProductRepository productRepository = mock(ProductRepository.class);
		when(productRepository.findById(productId))
			.thenReturn(Optional.of(testProduct));

		ProductService productService = new ProductService(productRepository, null);
		Product product = productService.getProduct(productId);
		Assertions.assertEquals(testProduct.getId(), product.getId());
		Assertions.assertEquals(testProduct.getName(), product.getName());
		Assertions.assertEquals(testProduct.getPrice(), product.getPrice());
	}

	@Test
	public void testCreateProduct() {
		ProductRepository productRepository = mock(ProductRepository.class);
		ProductRequest productRequest = new ProductRequest();
		productRequest.setName("he");
		productRequest.setPrice(34);
		
		ProductService productService = new ProductService(productRepository, null);
		ProductResponse productResponse = productService.createProduct(productRequest);
		verify(productRepository).insert(any(Product.class));
		Assertions.assertEquals(productResponse.getName(), productRequest.getName());
		Assertions.assertEquals(productResponse.getPrice(), productRequest.getPrice());
	}
}

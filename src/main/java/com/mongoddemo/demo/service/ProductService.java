package com.mongoddemo.demo.service;

import com.mongoddemo.demo.aop.ActionType;
import com.mongoddemo.demo.aop.EntityType;
import com.mongoddemo.demo.aop.SendEmail;
import com.mongoddemo.demo.converter.ProductConverter;
import com.mongoddemo.demo.entity.Product;
import com.mongoddemo.demo.exception.NotFoundException;
import com.mongoddemo.demo.model.request.ProductQueryParameter;
import com.mongoddemo.demo.model.request.ProductRequest;
import com.mongoddemo.demo.model.response.ProductResponse;
import com.mongoddemo.demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductService {

	private ProductRepository repository;

	public Product getProduct(String id) {
		return repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Can't find product."));
	}

	public ProductResponse getProductResponse(String id) {
		Product product = repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Can't find product."));
		return ProductConverter.toProductResponse(product);
	}

	@SendEmail(entity = EntityType.PRODUCT, action = ActionType.CREATE)
	public ProductResponse createProduct(ProductRequest request) {
		Product product = ProductConverter.toProduct(request);
		repository.insert(product);
		return ProductConverter.toProductResponse(product);
	}

	@SendEmail(entity = EntityType.PRODUCT, action = ActionType.UPDATE, idParamIndex = 0)
	public ProductResponse replaceProduct(String id, ProductRequest request) {
		Product oldProduct = getProduct(id);
		Product newProduct = ProductConverter.toProduct(request);
		newProduct.setId(oldProduct.getId());
		repository.save(newProduct);

		return ProductConverter.toProductResponse(newProduct);
	}

	@SendEmail(entity = EntityType.PRODUCT, action = ActionType.DELETE, idParamIndex = 0)
	public void deleteProduct(String id) {
		repository.deleteById(id);
	}

	public List<ProductResponse> getProductResponses(ProductQueryParameter param) {
		String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
		int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
		int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);
		Sort sort = genSortingStrategy(param.getOrderBy(), param.getSortRule());

		List<Product> products = repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, nameKeyword, sort);

		return products.stream()
			.map(ProductConverter::toProductResponse)
			.collect(Collectors.toList());
	}

	private Sort genSortingStrategy(String orderBy, String sortRule) {
		Sort sort = Sort.unsorted();
		if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
			Sort.Direction direction = Sort.Direction.fromString(sortRule);
			sort = Sort.by(direction, orderBy);
		}

		return sort;
	}

}
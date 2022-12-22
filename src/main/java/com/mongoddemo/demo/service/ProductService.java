package com.mongoddemo.demo.service;

import com.mongoddemo.demo.aop.ActionType;
import com.mongoddemo.demo.aop.EntityType;
import com.mongoddemo.demo.aop.SendEmail;
import com.mongoddemo.demo.converter.ProductConverter;
import com.mongoddemo.demo.entity.Product;
import com.mongoddemo.demo.exception.NotFoundException;
import com.mongoddemo.demo.model.request.ProductRequest;
import com.mongoddemo.demo.model.response.ExchangeRateResponse;
import com.mongoddemo.demo.model.response.ProductResponse;
import com.mongoddemo.demo.model.response.RateResponse;
import com.mongoddemo.demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@AllArgsConstructor
public class ProductService {
	private ProductRepository repository;

	private MongoTemplate mongoTemplate;

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

	public RateResponse getRates(String currencyFrom, String currencyTo) {

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
		RestTemplate restTemplate = new RestTemplate();
		restTemplate = new RestTemplateBuilder()
			.setConnectTimeout(Duration.ofSeconds(10))
			.additionalMessageConverters(converter)
			.build();

		String rateOpenUrl = "https://www.freeforexapi.com/api/live?pairs=";
		String url = rateOpenUrl + currencyFrom + currencyTo;
		System.out.println(url);

		HttpEntity<Object> httpEntity = new HttpEntity<>(null, null);
		ResponseEntity<ExchangeRateResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<ExchangeRateResponse>() {
		});
		ExchangeRateResponse exchangeRateResponse = responseEntity.getBody();
		assert exchangeRateResponse != null;
		ExchangeRateResponse.RateData rateData = exchangeRateResponse.getRates().get(currencyFrom + currencyTo);
		return new RateResponse(rateData.getRate());

	}

	private Sort genSortingStrategy(String orderBy, String sortRule) {
		Sort sort = Sort.unsorted();
		if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
			Sort.Direction direction = Sort.Direction.fromString(sortRule);
			sort = Sort.by(direction, orderBy);
		}

		return sort;
	}

	public Page<ProductResponse> getProductResponses(String keyword, Integer priceFrom, Integer priceTo, Pageable pageable) {
		Criteria criteria = new Criteria();
		if (keyword != null) {
			criteria.and("name").regex(keyword);
		}
		if (priceFrom != null && priceTo != null) {
			criteria.and("price").gte(priceFrom).lte(priceTo);
		}

		Query query = Query.query(criteria).with(pageable);

		Page<Product> productPage =
			PageableExecutionUtils.getPage(mongoTemplate.find(query, Product.class), pageable, () -> mongoTemplate.count(query.skip(0).limit(0), Product.class));

		List<Product> productList = productPage.getContent();
		List<ProductResponse> productResponseList = productList.stream()
			.map(ProductConverter::toProductResponse)
			.collect(Collectors.toList());

		return PageableExecutionUtils.getPage(productResponseList, pageable, productPage::getTotalElements);
	}
}
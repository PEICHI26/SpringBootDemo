package com.mongoddemo.demo.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongoddemo.demo.entity.AppUser;
import com.mongoddemo.demo.model.UserAuthority;
import com.mongoddemo.demo.model.request.AuthRequest;
import com.mongoddemo.demo.repository.AppUserRepository;
import com.mongoddemo.demo.repository.ProductRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//SpringBootTest.WebEnvironment.RANDOM_PORT 讓伺服器運行在隨機的埠號（port）上
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BaseTest {
	protected final ObjectMapper mapper = new ObjectMapper();
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final String USER_PASSWORD = "123456";
	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ProductRepository productRepository;
	@Autowired
	protected AppUserRepository appUserRepository;
	protected HttpHeaders httpHeaders = new HttpHeaders();

	@After
	public void clear() {
		appUserRepository.deleteAll();
		productRepository.deleteAll();
	}

	private AppUser createUser(String name, List<UserAuthority> authorities) {
		AppUser appUser = new AppUser();
		appUser.setName(name);
		appUser.setPassword(passwordEncoder.encode(USER_PASSWORD));
		appUser.setEmailAddress(name + " @gmail.com");
		appUser.setAuthorities(authorities);
		return appUserRepository.insert(appUser);
	}

	private void putHeader(String email) throws Exception {
		AuthRequest authRequest = new AuthRequest();
		authRequest.setUsername(email);
		authRequest.setPassword(USER_PASSWORD);
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		MvcResult mvcResult = mockMvc.perform(post("/auth")
				.headers(httpHeaders)
				.content(mapper.writeValueAsString(authRequest)))
			.andExpect(status().isOk())
			.andReturn();

		JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
		String token = jsonObject.getString("token");
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
	}

	protected void login() throws Exception {
		AppUser peggy = createUser("Peggy", Collections.singletonList(UserAuthority.ADMIN));
		putHeader(peggy.getEmailAddress());
	}

}

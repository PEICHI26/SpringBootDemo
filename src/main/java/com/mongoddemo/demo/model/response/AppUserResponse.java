package com.mongoddemo.demo.model.response;

import com.mongoddemo.demo.model.UserAuthority;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AppUserResponse {
	private String id;
	private String emailAddress;
	private String name;
	private List<UserAuthority> authorities;
}

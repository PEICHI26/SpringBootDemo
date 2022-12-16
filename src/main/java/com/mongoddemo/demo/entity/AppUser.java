package com.mongoddemo.demo.entity;

import com.mongoddemo.demo.model.UserAuthority;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("users")
@Getter
@Setter
public class AppUser extends Audit {
	private String id;
	private String emailAddress;
	private String password;
	private String name;
	private List<UserAuthority> authorities;
}

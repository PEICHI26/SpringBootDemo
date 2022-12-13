package com.mongoddemo.demo.model.request;

import com.mongoddemo.demo.model.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRequest {
	@NotBlank
	private String emailAddress;
	@NotBlank
	private String password;
	@NotBlank
	private String name;
	@NotEmpty
	private List<UserAuthority> authorities;
}

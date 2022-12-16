package com.mongoddemo.demo.util;

import com.mongoddemo.demo.entity.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdentity {
	private final UserDetailsImpl EMPTY_USER = new UserDetailsImpl(new AppUser());

	// JWTAuthenticationFilter 中，我們查詢出使用者詳情後，會將它加到 SecurityContext
	//能夠取得 id、名字、信箱以及是否匿名，其資料來源就是 Security Context。
	public UserDetailsImpl getUserInfo() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		return principal.equals("anonymousUser")
			? EMPTY_USER : (UserDetailsImpl) principal;
	}

	public String getId() {
		return getUserInfo().getId();
	}

	public boolean isAnonymous() {
		return getUserInfo().equals(EMPTY_USER);
	}

	public String getName() {
		return getUserInfo().getName();
	}

	public String getEmail() {
		return getUserInfo().getUsername();
	}
}

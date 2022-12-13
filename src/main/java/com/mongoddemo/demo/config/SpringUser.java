package com.mongoddemo.demo.config;

import com.mongoddemo.demo.entity.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SpringUser implements UserDetails {

	private AppUser appUser;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return appUser.getAuthorities().stream().map(userAuthority -> new SimpleGrantedAuthority(userAuthority.name())).collect(Collectors.toList());
	}

	public String getName() {
		return appUser.getName();
	}

	public String getId() {
		return appUser.getId();
	}

	@Override
	public String getPassword() {
		return appUser.getPassword();
	}

	@Override
	public String getUsername() {
		return appUser.getEmailAddress();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

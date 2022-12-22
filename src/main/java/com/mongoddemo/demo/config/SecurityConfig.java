package com.mongoddemo.demo.config;

import com.mongoddemo.demo.filter.JWTAuthenticationFilter;
import com.mongoddemo.demo.model.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	//UserDetailsService 這個介面來注入，Spring 會自動找到有實作這個介面的類別，也就是 UserDetailServiceImpl
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/users").hasAuthority(UserAuthority.ADMIN.name())
			.antMatchers(HttpMethod.GET, "/users/*").authenticated()
			.antMatchers(HttpMethod.POST, "/auth").permitAll()
			.antMatchers(HttpMethod.POST, "/auth/parse").permitAll()
			.antMatchers(HttpMethod.POST, "/users").permitAll()
			.anyRequest().authenticated()
			.and()
			//UsernamePasswordAuthenticationFilter 是用來處理基於帳號密碼的驗證（對應 Spring Security 的登入畫面）
			//後端每次收到請求時都執行固定的程式,比一般的 Filter 更優先執行
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			//Session 的機制停用
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			//關閉對 CSRF（跨站請求偽造）攻擊的防護。這樣 Security 機制才不會拒絕外部直接對 API 發出的請求，如 Postman 與前端。
			.csrf().disable();
	}

	//AuthenticationManager 接收到 UsernamePasswordAuthenticationToken 後，
	// 在底層會透過 SecurityConfig 中所配置的 UserDetailsService 與 PasswordEncoder 來協助驗證。
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}

	//註冊bean,給jwtService使用
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}

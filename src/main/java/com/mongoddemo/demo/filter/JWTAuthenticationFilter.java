package com.mongoddemo.demo.filter;

import com.mongoddemo.demo.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JWTService jwtService;

	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//將Access Token 連同請求一併傳送到伺服器,在請求標頭的「Authorization」欄位。格式是「Bearer」加一個空格，再加上 Access Token。
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null) {

			//只取token
			String token = authHeader.replace("Bearer ", "");

			//parseToken method 取得 claims
			Map<String, Object> claims = jwtService.parseToken(token);
			String username = (String) claims.get("username");
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			//在 Filter 中查詢使用者的目的，是為了將該次請求所代表的驗證後資料（Authentication）帶進 Security 的「Context」
			//為了將這個請求的使用者身份告訴伺服器,建立 UsernamePasswordAuthenticationToken 物件
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		//請求送達 Controller
		filterChain.doFilter(request, response);
	}
}

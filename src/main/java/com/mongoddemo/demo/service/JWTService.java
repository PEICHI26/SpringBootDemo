package com.mongoddemo.demo.service;

import com.mongoddemo.demo.model.request.AuthRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JWTService {

	@Value("${auth.key}")
	private String key;

	@Value("${auth.issuer}")
	private String issuer;
	//AuthenticationManager，它能幫助我們進行帳密驗證,去securityConfig註冊
	@Autowired
	private AuthenticationManager authenticationManager;

	public String generateToken(AuthRequest request) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
		//在底層會透過 SecurityConfig 中所配置的 UserDetailsService 與 PasswordEncoder 來協助驗證
		Authentication authentication1 = authenticationManager.authenticate(authentication);
		UserDetails userDetails = (UserDetails) authentication1.getPrincipal();

		//SeT Minute
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 480);

		//JWT 的內容可以存放自己的資料,這個 JWT 對外聲明的資料（claims）
		Claims claims = Jwts.claims();
		claims.put("username", userDetails.getUsername());
		claims.setExpiration(calendar.getTime());
		claims.setIssuer(issuer);

		//轉換成位元組陣列（byte[]）後傳入
		SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

		//Jwts.builder 方法得到一個 builder 物件它事先內建了 JWT 的標頭。我們將內容放入，並用密鑰簽名
		return Jwts.builder().setClaims(claims).signWith(secretKey).compact();
	}

	public Map<String, Object> parseToken(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

		//建立JwtParser需將密鑰傳入
		JwtParser parser = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build();

		//透過解析器的 parseClaimsJws 方法，可以解析含有簽名的 JWT（ 即 JWS）, getBody 方法，取得前面所認識的 Claims 物件。
		Claims claims = parser.parseClaimsJws(token).getBody();

		return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}

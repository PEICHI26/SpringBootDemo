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

	@Autowired
	private AuthenticationManager authenticationManager;

	public String generateToken(AuthRequest request) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
		Authentication authenticate = authenticationManager.authenticate(authentication);
		UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 100);

		Claims claims = Jwts.claims();
		claims.put("username", userDetails.getUsername());
		claims.setExpiration(calendar.getTime());
		claims.setIssuer(issuer);

		SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

		return Jwts.builder().setClaims(claims).signWith(secretKey).compact();
	}

	public Map<String, Object> parseToken(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
		JwtParser parser = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build();

		Claims claims = parser.parseClaimsJws(token).getBody();
		return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}

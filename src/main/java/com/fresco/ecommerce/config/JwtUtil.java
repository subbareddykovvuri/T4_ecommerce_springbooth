package com.fresco.ecommerce.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.service.UserAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	@Autowired
	private UserAuthService userDetails;
	
	private String secretKey = "yourSecretKey";  // Replace with a secure key

	public User getUser(final String token) {
		String username = extractUsername(token);
		return userDetails.loadUserByUsername(username);
//		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//        String username = claims.getSubject();
//        // Here you can implement logic to retrieve a User object by username
//        return new User();  // Implement actual user lookup from DB or service
	}

	public String generateToken(String username) {
		Map <String, Object> claims = new HashMap<>();
		return Jwts.builder()
				.setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Token valid for 10 hours
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
	}

	public boolean validateToken(final String token, UserDetails user) {
		return user.getUsername().equals(extractUsername(token)) && extractExpiration(token).after(new Date());
//		Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);  // Will throw exception if invalid
	}
	
	public Claims extractClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
	public Date extractExpiration(final String token) {
		return extractClaims(token).getExpiration();
	}
	
	public String extractUsername(final String token) {
		return extractClaims(token).getSubject();
	}
}

package com.fresco.ecommerce.config;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.service.UserAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Component
public class JwtUtil {
	
	private String signedSecretKey = "secretKey";
	
	@Autowired
	private UserAuthService authService;
	
	public User getUser(final String token) {
		Claims claims = Jwts
				.parser()
				.setSigningKey(signedSecretKey)
				.parseClaimsJws(token)
				.getBody();
		
		return authService.loadUserByUsername(claims.getSubject());

	}
	
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(SignatureAlgorithm.HS256, signedSecretKey)
				.compact();
	}
	public boolean validateToken(final String token) {
		if(Jwts.parser().setSigningKey(signedSecretKey).parseClaimsJws(token).getBody().getExpiration().after(new Date()) && getUser(token)!=null) {
			return true;
		}
		return false;
	}
	
	public String getCurrentUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated()) {
	        Object principal = authentication.getPrincipal();
	        if (principal instanceof UserDetails) {
	            return ((UserDetails) principal).getUsername(); // Returns the username of the authenticated user
	        } else {
	            return principal.toString(); // If principal is just a username string (not UserDetails)
	        }
	    }
	    return null; // No authenticated user
	}
}
package com.fresco.ecommerce.config;

import org.springframework.stereotype.Component;

import com.fresco.ecommerce.models.User;

@Component
public class JwtUtil {

	public User getUser(final String token) {
		return null;
	}

	public String generateToken(String username) {
		return null;
	}

	public void validateToken(final String token) {
	}
}

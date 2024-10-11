package com.fresco.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/consumer")
public class ConsumerController {

	@GetMapping("/cart")
	public ResponseEntity<Object> getCart() {
		return null;
	}

	@PostMapping("/cart")
	public ResponseEntity<Object> postCart() {
		return null;
	}

	@PutMapping("/cart")
	public ResponseEntity<Object> putCart() {
		return null;
	}

	@DeleteMapping("/cart")
	public ResponseEntity<Object> deleteCart() {
		return null;
	}

}
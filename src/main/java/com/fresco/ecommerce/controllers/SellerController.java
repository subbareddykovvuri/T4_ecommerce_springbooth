package com.fresco.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.ecommerce.models.Product;

@RestController
@RequestMapping("/api/auth/seller")
public class SellerController {
	@PostMapping("/product")
	public ResponseEntity<Object> postProduct() {
		return null;
	}

	@GetMapping("/product")
	public ResponseEntity<Object> getAllProducts() {
		return null;
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<Object> getProduct() {
		return null;
	}

	@PutMapping("/product")
	public ResponseEntity<Object> putProduct() {
		return null;
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<Product> deleteProduct() {
		return null;
	}
}
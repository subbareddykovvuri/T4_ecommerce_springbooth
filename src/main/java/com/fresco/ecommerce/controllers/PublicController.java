package com.fresco.ecommerce.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.ecommerce.models.Product;

@RestController
@RequestMapping("/api/public")
public class PublicController {

	@GetMapping("/product/search")
	public List<Product> getProducts() {
		return null;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login() {
		return null;
	}

}
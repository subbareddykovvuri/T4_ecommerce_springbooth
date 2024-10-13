package com.fresco.ecommerce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.ecommerce.config.JwtUtil;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.repo.CategoryRepo;
import com.fresco.ecommerce.repo.ProductRepo;
import com.fresco.ecommerce.repo.UserRepo;

@RestController
@RequestMapping("/api/auth/seller")
public class SellerController {
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@PostMapping("/product")
	public ResponseEntity<Object> postProduct(@RequestBody Product p) {
		p.setCategory(categoryRepo.findByCategoryName(p.getCategory().getCategoryName()).get());
		p.setSeller(userRepo.findByUsername(jwtUtil.getCurrentUser()).get());
		Product p1 = productRepo.saveAndFlush(p);
		 // Manually construct the URL for the newly created resource
        String newResourceUrl = "http://localhost:8000/api/auth/seller/product/" + p1.getProductId();

        // Set the Location header for the redirect
        HttpHeaders headers = new HttpHeaders();
        headers.set("Location", newResourceUrl);
		return new ResponseEntity<Object>(headers,HttpStatus.CREATED);
	}

	@GetMapping("/product")
	public ResponseEntity<Object> getAllProducts() {
		List<Product> productList  = productRepo.findBySellerUserId(userRepo.findByUsername(jwtUtil.getCurrentUser()).get().getUserId());
		return new ResponseEntity<Object>(productList,HttpStatus.OK);
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<Object> getProduct(@PathVariable Integer productId) {
		
		Optional<Product> p = productRepo.findBySellerUserIdAndProductId(userRepo.findByUsername(jwtUtil.getCurrentUser()).get().getUserId(), productId);
		if(p.isPresent()) {
			return new ResponseEntity<Object>(p.get(),HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/product")
	public ResponseEntity<Object> putProduct(@RequestBody Product p) {
		User user = userRepo.findByUsername(jwtUtil.getCurrentUser()).get();
		Optional<Product> p1 = productRepo.findBySellerUserIdAndProductId(user.getUserId(), p.getProductId());
		if (p1.isPresent()) {
			System.out.println(p1);
			Product p2 = p1.get();
			p2.setProductName(p.getProductName());
			p2.setPrice(p.getPrice());
			p2.setCategory(categoryRepo.findByCategoryName(p.getCategory().getCategoryName()).get());
			productRepo.saveAndFlush(p2);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<Product> deleteProduct(@PathVariable Integer productId) {
		User user = userRepo.findByUsername(jwtUtil.getCurrentUser()).get();
		Optional<Product> p1 = productRepo.findBySellerUserIdAndProductId(user.getUserId(),productId);
		if (p1.isPresent()) {
			productRepo.deleteById(productId);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
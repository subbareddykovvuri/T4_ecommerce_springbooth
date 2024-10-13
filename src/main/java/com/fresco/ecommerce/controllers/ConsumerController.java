package com.fresco.ecommerce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.ecommerce.config.JwtUtil;
import com.fresco.ecommerce.models.Cart;
import com.fresco.ecommerce.models.CartProduct;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.repo.CartProductRepo;
import com.fresco.ecommerce.repo.CartRepo;
import com.fresco.ecommerce.repo.ProductRepo;
import com.fresco.ecommerce.repo.UserRepo;

@RestController
@RequestMapping("/api/auth/consumer")
public class ConsumerController {
	
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private CartProductRepo cartProductRepo;
	
	@Autowired
	private UserRepo userRepo;
	

	@GetMapping("/cart")
	public ResponseEntity<Object> getCart() {

		Optional<Cart> cart = cartRepo.findByUserUsername(jwtUtil.getCurrentUser());
		if (cart.isPresent()) {
			return new ResponseEntity<>(cart.get(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/cart")
	public ResponseEntity<Object> postCart(@RequestBody Product p) {
		Optional<Cart> cart = cartRepo.findByUserUsername(jwtUtil.getCurrentUser());
		if (cart.isPresent()) {
			Optional<Product> p1 = productRepo.findById(p.getProductId());
			if(p1.isEmpty()) {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
			Cart c = cart.get();
			boolean flag = false;
			List<CartProduct> cplist = c.getCartProducts();
			for(CartProduct cp : cplist) {
				if (cp.getProduct().equals(p1.get())) {
					flag = true;
				}
			}
			if(flag) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			CartProduct cp1 = new CartProduct();
			cp1.setQuantity(1);
			cp1.setProduct(p1.get());
			cp1.setCart(c);
			cplist.add(cp1);
			c.setCartProducts(cplist);
			cartProductRepo.save(cp1);
			cartRepo.save(c);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/cart")
	public ResponseEntity<Object> putCart(@RequestBody CartProduct cp) {
		
		Optional<CartProduct> cp1 = cartProductRepo.findByCartUserUserIdAndProductProductId(userRepo.findByUsername(jwtUtil.getCurrentUser()).get().getUserId() , cp.getProduct().getProductId());
		if(cp1.isEmpty()) {
			if(cp.getQuantity()>0) {
				cartProductRepo.save(cp);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		else {
			CartProduct cp2 = cp1.get();
			if (cp.getQuantity()<=0) {
				cartProductRepo.deleteByCartUserUserIdAndProductProductId(userRepo.findByUsername(jwtUtil.getCurrentUser()).get().getUserId() , cp.getProduct().getProductId());
				return new ResponseEntity<Object>(HttpStatus.OK);
			}
			cp2.setQuantity(cp.getQuantity());
			cartProductRepo.save(cp2);
			return new ResponseEntity<Object> (HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/cart")
	public ResponseEntity<Object> deleteCart(@RequestBody Product p) {
		Optional<CartProduct> cp1 = cartProductRepo.findByCartUserUserIdAndProductProductId(userRepo.findByUsername(jwtUtil.getCurrentUser()).get().getUserId() , p.getProductId());
		if(cp1.isPresent()) {
			cartProductRepo.delete(cp1.get());
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
	}

}
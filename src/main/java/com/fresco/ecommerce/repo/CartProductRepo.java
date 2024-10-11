package com.fresco.ecommerce.repo;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.ecommerce.models.CartProduct;

@Repository
public interface CartProductRepo extends JpaRepository<CartProduct, Integer> {
	Optional<CartProduct> findByCartUserUserIdAndProductProductId(Integer userId, Integer productId);

	@Transactional
	void deleteByCartUserUserIdAndProductProductId(Integer userId, Integer productId);
}
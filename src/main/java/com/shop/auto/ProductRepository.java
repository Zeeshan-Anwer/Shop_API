package com.shop.auto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByNameContaining(String name);

	List<Product> findByLocation(String location);
}

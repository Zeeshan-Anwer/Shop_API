package com.shop.auto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

	@Service
	public class ProductServiceImpl implements ProductService {

	    @Autowired
	    private ProductRepository productRepository;

	    public Product addProduct(Product product) {
	        return productRepository.save(product);
	    }

	    public List<Product> getAllProducts() {
	        return productRepository.findAll();
	    }

	    public List<Product> searchProductByName(String name) {
	        return productRepository.findByNameContaining(name);
	    }

	    public List<Product> searchProductByLocation(String location) {
	        return productRepository.findByLocation(location);
	    }
	}




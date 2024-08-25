package com.shop.auto;

import java.util.List;
import java.util.List;

public interface ProductService {
	
	Product addProduct(Product product);

	List<Product> getAllProducts();

	List<Product> searchProductByName(String name);

	List<Product> searchProductByLocation(String location);
}

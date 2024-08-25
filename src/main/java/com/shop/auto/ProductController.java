package com.shop.auto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")  // Enable CORS for all origins, adjust as necessary
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("buyPrice") double buyPrice,
            @RequestParam("minimumSellPrice") double minimumSellPrice,
            @RequestParam("normalPrice") double normalPrice,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("pictures") List<MultipartFile> pictures) throws IOException {

        List<byte[]> pictureByteList = new ArrayList<>();
        for (MultipartFile picture : pictures) {
            pictureByteList.add(convertFileToBytes(picture));
        }

        Product product = new Product();
        product.setName(name);
        product.setBuyPrice(buyPrice);
        product.setMinimumSellPrice(minimumSellPrice);
        product.setNormalPrice(normalPrice);
        product.setDescription(description);
        product.setLocation(location);
        product.setPictures(pictureByteList);

        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    private byte[] convertFileToBytes(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    @GetMapping("/viewAllProduct")
    public ResponseEntity<List<Product>> viewAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProductByName(name));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Product>> searchProductByLocation(@PathVariable String location) {
        return ResponseEntity.ok(productService.searchProductByLocation(location));
    }
}

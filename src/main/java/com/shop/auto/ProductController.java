package com.shop.auto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@CrossOrigin(origins = "*")  // Enable CORS for all origins
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private GoogleDriveService googleDriveService;

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("buyPrice") double buyPrice,
            @RequestParam("minimumSellPrice") double minimumSellPrice,
            @RequestParam("normalPrice") double normalPrice,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("pictures") List<MultipartFile> pictures) {

        try {
            List<String> pictureUrls = new ArrayList<>();
            for (MultipartFile picture : pictures) {
                String fileUrl = googleDriveService.uploadFile((MultipartFile) convertMultipartFileToFile(picture));
                pictureUrls.add(fileUrl);
                
            }

            Product product = new Product();
            product.setName(name);
            product.setBuyPrice(buyPrice);
            product.setMinimumSellPrice(minimumSellPrice);
            product.setNormalPrice(normalPrice);
            product.setDescription(description);
            product.setLocation(location);
            product.setPictureUrls(pictureUrls);

            Product savedProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (IOException e) {
            logger.error("Error uploading file to Google Drive: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("Error adding product: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }

    @GetMapping("/viewAllProduct")
    public ResponseEntity<List<Product>> viewAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductByName(@RequestParam String name) {
        logger.info("Received request for name: {}", name);
        if (name == null || name.trim().isEmpty()) {
            logger.warn("No name found : {}", name);
            return ResponseEntity.badRequest().body(List.of()); // Return an empty list if no name provided
        }
        List<Product> products = productService.searchProductByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Product>> searchProductByLocation(@PathVariable String location) {
        logger.info("Received request for location: {}", location);
        if (location == null || location.trim().isEmpty()) {
            logger.warn("No location found : {}", location);
            return ResponseEntity.badRequest().body(List.of()); // Return an empty list if no location provided
        }
        List<Product> products = productService.searchProductByLocation(location);
        return ResponseEntity.ok(products);
    }
}

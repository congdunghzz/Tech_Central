package com.example.techcentral.controller;

import com.example.techcentral.models.Product;
import com.example.techcentral.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProduct(){
        return productService.getAllProduct();
    }
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        System.out.println("Controller: " + product.getProductDetail());
        Product result = productService.addProduct(product);
        return new ResponseEntity<>(result ,HttpStatus.CREATED);
    }
}

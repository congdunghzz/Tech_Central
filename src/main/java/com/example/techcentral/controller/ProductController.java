package com.example.techcentral.controller;

import com.example.techcentral.dto.ProductDTO;
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
    public ResponseEntity<List<ProductDTO>> getAllProduct(){
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findOne(@PathVariable Long id) {
        ProductDTO result = productService.findOneById(id);
        if (result != null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO result = productService.addProduct(productDTO);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }else return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        boolean result = productService.deleteProduct(id);
        if (result){
            return new ResponseEntity<>("delete successfully", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Encounter an error", HttpStatus.BAD_REQUEST);
        }
    }
}
package com.example.techcentral.controller;

import com.example.techcentral.dto.product.ProductDTO;
import com.example.techcentral.dto.product.ProductRequest;
import com.example.techcentral.models.ProductImage;
import com.example.techcentral.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/category/{name}")
    public ResponseEntity<List<ProductDTO>> getAllProductByCategory(@PathVariable String name){
        return new ResponseEntity<>(productService.getProductByCategory(name), HttpStatus.OK);
    }

    @GetMapping("/brand/{name}")
    public ResponseEntity<List<ProductDTO>> getAllProductByBrand(@PathVariable String name){
        return new ResponseEntity<>(productService.getProductByBrand(name), HttpStatus.OK);
    }

    @GetMapping("/category/{category}/brand/{brand}")
    public ResponseEntity<List<ProductDTO>> getAllProductByCategory(@PathVariable String category,
                                                                    @PathVariable String brand){
        return new ResponseEntity<>(productService.getProductByCategoryAndBrand(category, brand), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findOne(@PathVariable Long id) {
        ProductDTO result = productService.findOneById(id);
        if (result != null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> addProduct(@ModelAttribute ProductRequest request) {
        System.out.println( "Controller Product detail: " + request.productDetail());
        System.out.println( "Controller Product images: " + request.images());
        ProductDTO result = productService.addProduct(request);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

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

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductDTO productDTO){

        ProductDTO result = productService.editProduct(id,productDTO);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{id}/image", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> postProductImage(@PathVariable Long id,
                                                       @RequestParam(value = "images") List<MultipartFile> images){
        System.out.println( "Controller Post Images: " + images);
        ProductDTO product = productService.addImages(id, images);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<ProductDTO> deleteProductImage(@PathVariable Long id,
                                                        @RequestBody List<ProductImage> images){
        ProductDTO product = productService.deleteImages(id, images);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}

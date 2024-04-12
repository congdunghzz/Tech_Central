package com.example.techcentral.controller;

import com.example.techcentral.dto.product.ProductDTO;
import com.example.techcentral.dto.product.ProductRequest;
import com.example.techcentral.models.ProductImage;
import com.example.techcentral.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProduct(
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size
    ){
        return new ResponseEntity<>(
                productService.getAllProduct(page.orElse(1), size.orElse(1000)),
                HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProduct(
            @RequestParam(value = "key", required = false) String name,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size
    ){
        return new ResponseEntity<>(
                productService.searchForName(name ,page.orElse(1), size.orElse(1000)),
                HttpStatus.OK);
    }

    @GetMapping("newProduct")
    public ResponseEntity<List<ProductDTO>> getNewProduct (@RequestParam(value = "size") Optional<Integer> size){
        return ResponseEntity.ok(productService.getLatestProducts(size.orElse(8)));
    }

    @GetMapping("/category")
    public ResponseEntity<Page<ProductDTO>> getAllProductByCategory(@RequestParam(value ="name", required = false) String name,
                                                                    @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                                    @RequestParam(value = "size", required = false) Optional<Integer> size
    ){
        return new ResponseEntity<>(
                productService.getProductByCategory(name,  page.orElse(1), size.orElse(1000)),
                        HttpStatus.OK);
    }

    @GetMapping("/brand")
    public ResponseEntity<Page<ProductDTO>> getAllProductByBrand(@RequestParam(value = "name", required = false) String name,
                                                                 @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                                 @RequestParam(value = "size", required = false) Optional<Integer> size
    ){
        return new ResponseEntity<>(
                productService.getProductByBrand(name, page.orElse(1), size.orElse(1000)),
                HttpStatus.OK);
    }

    @GetMapping("/category/brand")
    public ResponseEntity<Page<ProductDTO>> getAllProductByCategoryAndBrand(@RequestParam(value ="category", required = false) String category,
                                                                    @RequestParam(value ="brand", required = false) String brand,
                                                                    @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                                    @RequestParam(value = "size", required = false) Optional<Integer> size
    ){
        return new ResponseEntity<>(productService.getProductByCategoryAndBrand(category, brand,
                                                page.orElse(1), size.orElse(1000)),
                                    HttpStatus.OK);
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

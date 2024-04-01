package com.example.techcentral.controller;

import com.example.techcentral.models.Brand;
import com.example.techcentral.models.Category;
import com.example.techcentral.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<List<Brand>> getAll(){
        List<Brand> result = brandService.getAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getById(@PathVariable Long id){
        Brand brand = brandService.getById(id);
        return ResponseEntity.ok().body(brand);
    }

    @PostMapping
    public ResponseEntity<Brand> addNewCategory(@RequestBody Brand brand){
        Brand result = brandService.addBrand(brand);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateCategory(@PathVariable Long id ,@RequestBody Brand brand){
        Brand result = brandService.editName(id, brand);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteCategory(@PathVariable Long id){
        brandService.deleteById(id);
        return HttpStatus.ACCEPTED;
    }
}

package com.example.techcentral.controller;

import com.example.techcentral.models.Category;
import com.example.techcentral.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll(){
        List<Category> result = categoryService.getAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id){
        Category category = categoryService.getById(id);
        return ResponseEntity.ok().body(category);
    }

    @PostMapping
    public ResponseEntity<Category> addNewCategory(@RequestBody Category category){
        Category result = categoryService.addCategory(category);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id ,@RequestBody Category category){
        Category result = categoryService.editName(id, category);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteCategory(@PathVariable Long id){
        categoryService.deleteById(id);
        return HttpStatus.ACCEPTED;
    }
}

package com.example.techcentral.service;

import com.example.techcentral.dao.CategoryRepository;
import com.example.techcentral.dao.ProductRepository;
import com.example.techcentral.models.Category;
import com.example.techcentral.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    public Product addProduct(Product product){
        Product productResult;
        if (product.getCategory() != null) {
            Category category = categoryRepository.findByName(product.getCategory().getName());
            if (category != null) {
                category.getProducts().add(product);
                categoryRepository.save(category);
            }
            productResult = productRepository.save(product);
            return productResult;
        }
        System.out.println("Category Is  Null");
        return null;
    }
}

package com.example.techcentral.dao;

import com.example.techcentral.models.Brand;
import com.example.techcentral.models.Category;
import com.example.techcentral.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(Category category);
    List<Product> findAllByBrand(Brand brand);
    List<Product> findAllByCategoryAndBrand(Category category, Brand brand);
}

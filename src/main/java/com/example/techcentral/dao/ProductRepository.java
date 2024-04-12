package com.example.techcentral.dao;

import com.example.techcentral.models.Brand;
import com.example.techcentral.models.Category;
import com.example.techcentral.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategory(Category category, Pageable pageable);
    Page<Product> findAllByBrand(Brand brand, Pageable pageable);
    Page<Product> findAllByCategoryAndBrand(Category category, Brand brand, Pageable pageable);
}

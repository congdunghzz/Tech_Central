package com.example.techcentral.dao;

import com.example.techcentral.models.ProductImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.beans.Transient;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    @Transactional
    void deleteByUrl(String url);
}

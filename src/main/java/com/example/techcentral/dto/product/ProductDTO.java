package com.example.techcentral.dto.product;

import com.example.techcentral.models.ProductDetail;
import com.example.techcentral.models.ProductImage;
import jakarta.persistence.Column;

import java.util.List;

public record ProductDTO(
        Long id,
        String name,
        double price,
        List<ProductImage> productImages,
        ProductDetail productDetail,
        String category,
        String brand
    ) {

}

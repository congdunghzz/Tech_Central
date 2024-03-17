package com.example.techcentral.dto.product;

import com.example.techcentral.models.ProductDetail;
import com.example.techcentral.models.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductRequest(
        String name,
        double price,
        ProductDetail productDetail,
        List<MultipartFile> images,
        String category,
        String brand
) {

}


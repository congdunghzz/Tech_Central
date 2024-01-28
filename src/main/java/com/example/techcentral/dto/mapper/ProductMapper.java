package com.example.techcentral.dto.mapper;

import com.example.techcentral.dto.ProductDTO;

import com.example.techcentral.models.Product;

import java.util.List;
import java.util.stream.Collectors;


public class ProductMapper {
    public static ProductDTO TransferToProductDTO(Product product){
        return new ProductDTO  (product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getProductImages(),
                                product.getProductDetail(),
                                product.getCategory().getId());
    }

    public static List<ProductDTO> TransferToProductDTOs(List<Product> products){
        return products.stream().map(ProductMapper::TransferToProductDTO).toList();
    }
}

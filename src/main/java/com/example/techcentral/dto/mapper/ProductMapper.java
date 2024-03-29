package com.example.techcentral.dto.mapper;

import com.example.techcentral.dto.product.ProductDTO;

import com.example.techcentral.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductMapper {
    public static ProductDTO TransferToProductDTO(Product product){
        return new ProductDTO  (product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getProductImages(),
                                product.getProductDetail(),
                                product.getCategory().getName(),
                                product.getBrand().getName());
    }

    public static List<ProductDTO> TransferToProductDTOs(List<Product> products){
        return products.stream().map(ProductMapper::TransferToProductDTO).toList();
    }
}

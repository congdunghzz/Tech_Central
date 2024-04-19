package com.example.techcentral.dto.mapper;

import com.example.techcentral.dto.product.ProductDTO;

import com.example.techcentral.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductMapper {
    public static ProductDTO TransferToProductDTO(Product product){
        return new ProductDTO  (product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getStock(),
                                product.getProductImages(),
                                product.getProductDetail(),
                                product.getCategory().getName(),
                                product.getBrand().getName());
    }

    public static List<ProductDTO> TransferToProductDTOs(List<Product> products){
        return products.stream().map(ProductMapper::TransferToProductDTO).toList();
    }

    public static Page<ProductDTO> TransferToProductDTOPage(Page<Product> products){
        List<ProductDTO> dtoList = products.getContent().stream().map(ProductMapper::TransferToProductDTO).toList();
        return new PageImpl<>(dtoList, products.getPageable(), products.getTotalElements());
    }
}

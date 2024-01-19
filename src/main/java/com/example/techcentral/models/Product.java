package com.example.techcentral.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRODUCT_TITLE")
    private String title;

    @Column(name = "PRODUCT_PRICE")
    private double price;

    @OneToMany(mappedBy = "products")
    private List<ProductImage> productImages;

    @ManyToOne
    @JoinColumn (name = "CATEGORY_ID")
    Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DETAIL_ID")
    ProductDetail productDetail;
}

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
@ToString
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRODUCT_TITLE")
    private String name;

    @Column(name = "PRODUCT_PRICE")
    private double price;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductImage> productImages;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn (name = "CATEGORY_ID")
    Category category;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "DETAIL_ID")
    ProductDetail productDetail;
}

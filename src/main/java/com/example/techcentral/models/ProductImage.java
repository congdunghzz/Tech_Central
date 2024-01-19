package com.example.techcentral.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_image")
public class ProductImage {
    @Id
    @Column(name = "PRODUCT_IMAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "URL")
    private String url;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product products;
}

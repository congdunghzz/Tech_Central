package com.example.techcentral.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "product_detail")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CPU")
    private String cpu;

    @Column(name = "RAM")
    private int ram;

    @Column(name = "ROM")
    private int rom;

    @Column(name = "SCREEN")
    private double screen;

    @Column(name = "RESOLUTION")
    private String resolution;

    @Column(name = "MATERIAL")
    private String material;

    @Column(name = "COLOR")
    private String color;

    @OneToOne(mappedBy = "productDetail",cascade = CascadeType.PERSIST)
    @JsonIgnore
    Product product;

}

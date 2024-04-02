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
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @Column(name = "ORDER_DETAIL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="PRODUCT_ID")
    private Long productId;

    @Column(name="PRODUCT_NAME")
    private String productName;

    @Column(name="PRODUCT_PRICE")
    private double productPrice;

    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @Column(name = "COST", nullable = false)
    private double cost;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ORDER_ID")
    private Order order;
}

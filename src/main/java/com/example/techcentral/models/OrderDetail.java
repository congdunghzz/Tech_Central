package com.example.techcentral.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @Column(name = "ORDER_DETAIL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @Column(name = "COST", nullable = false)
    private int cost;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;
}

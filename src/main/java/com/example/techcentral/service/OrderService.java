package com.example.techcentral.service;

import com.example.techcentral.dao.OrderDetailRepository;
import com.example.techcentral.dao.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }


}

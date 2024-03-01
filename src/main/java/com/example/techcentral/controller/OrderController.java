package com.example.techcentral.controller;

import com.example.techcentral.models.Order;
import com.example.techcentral.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/user/{id}")
    public List<Order> findByUser (@PathVariable Long id, Authentication authentication){
        /*User user = (User) authentication.getPrincipal();*/

        return orderService.findAllByUser(id);
    }

    @GetMapping("/{id}")
    public Order findById (@PathVariable Long id, Authentication authentication){
        return orderService.findOneById(id);
    }
}

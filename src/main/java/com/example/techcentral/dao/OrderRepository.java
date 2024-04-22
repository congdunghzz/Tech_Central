package com.example.techcentral.dao;

import com.example.techcentral.enums.OrderStatus;
import com.example.techcentral.models.Order;
import com.example.techcentral.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);
    Page<Order> findByOrderStatusOrderByOrderDateDesc(OrderStatus status, Pageable pageable);
}

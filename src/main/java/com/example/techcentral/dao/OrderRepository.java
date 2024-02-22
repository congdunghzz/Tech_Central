package com.example.techcentral.dao;

import com.example.techcentral.models.Order;
import com.example.techcentral.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByOrderDateDesc();
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

}

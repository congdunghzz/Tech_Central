package com.example.techcentral.service;

import com.example.techcentral.dao.OrderRepository;
import com.example.techcentral.dao.ProductRepository;
import com.example.techcentral.dao.UserRepository;
import com.example.techcentral.dto.order.request.OrderDetailRequestDTO;
import com.example.techcentral.dto.order.request.OrderRequest;
import com.example.techcentral.enums.OrderStatus;
import com.example.techcentral.models.Order;
import com.example.techcentral.models.OrderDetail;
import com.example.techcentral.models.Product;
import com.example.techcentral.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Order findOneById (Long orderId){
        Optional<Order> result = orderRepository.findById(orderId);
        return result.orElse(null);
    }

    public List<Order> findAll (){
        return orderRepository.findAll();
    }

    public List<Order> findAllByUser(Long userId){
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    public Order createOrder(OrderRequest request, Long userId){

        // check user if it is present
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) return null;

        List<OrderDetail> details = new ArrayList<>();
        double totalCost = 0;

        // get order detail list
        for (OrderDetailRequestDTO item : request.getOrderDetailRequestDTOs()){
            Optional<Product> product = productRepository.findById(item.productId());

            //check product if it is present
            if (product.isEmpty()) return null;

            totalCost += product.get().getPrice() * item.amount();
            details.add(OrderDetail
                    .builder()
                    .product(product.get())
                    .amount(item.amount())
                    .cost(product.get().getPrice() * item.amount())
                    .build());
        }
        Order newOrder = Order
                .builder()
                .name(request.getOrderDTO().name())
                .address(request.getOrderDTO().Address())
                .phone(request.getOrderDTO().phone())
                .orderDate(new Date(System.currentTimeMillis()))
                .totalCost(totalCost)
                .orderStatus(OrderStatus.PROCESSING)
                .orderDetails(details)
                .user(user.get())
                .build();
        user.get().getOrders().add(newOrder);
        return orderRepository.save(newOrder);
    }

    public Order updateStatus(Long orderId, OrderStatus status){
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) return null;
        order.get().setOrderStatus(status);
        return orderRepository.save(order.get());
    }

    public boolean deleteOrder (Long orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) return false;
        try{
            orderRepository.deleteById(orderId);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

package com.example.techcentral.service;

import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.example.techcentral.ExceptionHandler.ProductStockException;
import com.example.techcentral.ExceptionHandler.UnAuthorizedException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        if (result.isEmpty())
            throw new NotFoundException("Order with id: " +orderId+ " is not found");
        return result.get();
    }

    public Page<Order> findAll (int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findAllByOrderByOrderDateDesc(pageable);
    }

    public List<Order> findAllByUser(Long userId){
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    //PROCESSING, SHIPPING ,FINISHED, CANCELED
    public Page<Order> findAllByStatus(String status, int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        OrderStatus st;
        switch (status.toUpperCase()){
            case "PROCESSING": {
                st = OrderStatus.PROCESSING;
                break;
            }
            case "SHIPPING": {
                st = OrderStatus.SHIPPING;
                break;
            }
            case "FINISHED": {
                st = OrderStatus.FINISHED;
                break;
            }
            case "CANCELED": {
                st = OrderStatus.CANCELED;
                break;
            }
            default: {
                st = null;
                break;
            }
        }
        if (st == null){
            return orderRepository.findAllByOrderByOrderDateDesc(pageable);
        }
        return orderRepository.findByOrderStatusOrderByOrderDateDesc(st, pageable);
    }


    public Order createOrder(OrderRequest request, Long userId){

        // check user if it is present
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException("User with id: " +userId+" was not found");

        Order newOrder = new Order();
        newOrder.setUser(user.get());
        List<OrderDetail> details = new ArrayList<>();

        List<Product> products = new ArrayList<>();
        double totalCost = 0;

        // get order detail list

        for (OrderDetailRequestDTO item : request.getOrderDetailRequestDTOs()){
            Optional<Product> product = productRepository.findById(item.productId());

            //check product if it is present
            if (product.isEmpty())
                throw new NotFoundException("Product with id: " +item.productId()+ " is not found");
            products.add(product.get());

            totalCost += product.get().getPrice() * item.amount();
            details.add(OrderDetail
                    .builder()
                    .productId(product.get().getId())
                    .productName(product.get().getName())
                    .productPrice(product.get().getPrice())
                    .amount(item.amount())
                    .order(newOrder)
                    .cost(product.get().getPrice() * item.amount())
                    .build());
        }
        newOrder.setName(request.getName());
        newOrder.setAddress(request.getAddress());
        newOrder.setPhone(request.getPhone());
        newOrder.setOrderDate(new Date(System.currentTimeMillis()));
        newOrder.setTotalCost(totalCost);
        newOrder.setOrderStatus(OrderStatus.PROCESSING);
        newOrder.setOrderDetails(details);

        user.get().getOrders().add(newOrder);

        products.forEach(product -> {
            int quantityOfItem = 0;

            for (OrderDetailRequestDTO item : request.getOrderDetailRequestDTOs()) {

                if (product.getId().equals(item.productId())){
                    if (product.getStock() < item.amount()){
                        throw new ProductStockException("The product is out of stock");
                    }
                    quantityOfItem = item.amount();
                    break;
                }
            }
            product.setStock(product.getStock() - quantityOfItem);
            productRepository.save(product);
        });
        return orderRepository.save(newOrder);
    }

    public Order updateStatus(Long orderId, OrderStatus status){

        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty())
            throw new NotFoundException("Order with id: " +orderId+ " is not found");
        if (order.get().getOrderStatus() == status) return null;

        if (status == OrderStatus.CANCELED){
            order.get().getOrderDetails().forEach(detail ->{
                Product product = productRepository.findById(detail.getProductId()).get();
                product.setStock(product.getStock() + detail.getAmount());
                productRepository.save(product);
            });
        }

        order.get().setOrderStatus(status);
        return orderRepository.save(order.get());
    }

    public void deleteOrderForAdmin(Long orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty())
            throw new NotFoundException("Order with id: " +orderId+ " is not found");
        try{
            orderRepository.deleteById(orderId);
        }catch (Exception e){
            throw new NotFoundException("Order with id: " +orderId+ " is not found");
        }
    }

    public void deleteOrderForCustomer (Long orderId, Long userId){
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty())
            throw new NotFoundException("Order with id: " +orderId+" was not found");

        if (order.get().getOrderStatus() != OrderStatus.PROCESSING ||
                !Objects.equals(userId, order.get().getUser().getId())){

            throw new UnAuthorizedException("You are not permitted to delete order: " +orderId);
        }
            orderRepository.deleteById(orderId);

    }
}

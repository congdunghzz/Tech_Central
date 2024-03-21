package com.example.techcentral.controller;

import com.example.techcentral.ExceptionHandler.UnAuthorizedException;
import com.example.techcentral.dto.order.request.OrderRequest;
import com.example.techcentral.enums.OrderStatus;
import com.example.techcentral.enums.UserRole;
import com.example.techcentral.models.Order;
import com.example.techcentral.service.OrderService;
import com.example.techcentral.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private final OrderService orderService;
    @Autowired
    private UserService userService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrderForAdmin (){
        List<Order> result = orderService.findAll();
        result.stream().forEach(item -> System.out.println(item.getOrderDetails()));
        return ResponseEntity.ok(result);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> findByUser (@PathVariable Long id){
        List<Order> result = orderService.findAllByUser(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById (@PathVariable Long id){
        Order order = orderService.findOneById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> generateOrder(@RequestBody OrderRequest orderRequest,
                                               HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        Order order;
        long userId = -1L;
        for (Cookie cookieItem: cookies) {
            if (cookieItem.getName().equals("userId")){
                userId = Long.parseLong(cookieItem.getValue());
            }
        }

        if (userId != 1){
            order = orderService.createOrder(orderRequest, userId);
        }else {
            throw new UnAuthorizedException("You is not permitted to do this action");
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatusByAdmin(@PathVariable Long id,
                                                     @RequestBody OrderStatus status){
        Order order = orderService.updateStatus(id, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id, HttpServletRequest request){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        Cookie[] cookies = request.getCookies();
        long userId = -1L;
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("userId")){
                userId = Long.parseLong(cookie.getValue());
            }
        }

        if (userId == -1L)
            throw new UnAuthorizedException("You are not permitted to do this action");

        if (userService.getById(userId).role() == UserRole.ADMIN){
            orderService.deleteOrderForAdmin(id);
            httpStatus = HttpStatus.OK;
        }
        else if((userService.getById(userId).role() == UserRole.USER)){
            orderService.deleteOrderForCustomer(id, userId);
            httpStatus = HttpStatus.OK;
        }
        return httpStatus;
    }
}

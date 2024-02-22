package com.example.techcentral.dto.order.request;

import com.example.techcentral.enums.OrderStatus;

import java.sql.Date;

public record OrderRequestDTO(
         String Address,
         String phone,
         String name
){
}

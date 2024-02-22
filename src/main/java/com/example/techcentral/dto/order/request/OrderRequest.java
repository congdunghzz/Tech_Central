package com.example.techcentral.dto.order.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private OrderRequestDTO orderDTO;
    private List<OrderDetailRequestDTO> orderDetailRequestDTOs;
}

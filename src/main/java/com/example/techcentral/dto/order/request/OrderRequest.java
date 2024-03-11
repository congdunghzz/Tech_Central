package com.example.techcentral.dto.order.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String address;
    private String phone;
    private String name;
    private List<OrderDetailRequestDTO> orderDetailRequestDTOs;
}

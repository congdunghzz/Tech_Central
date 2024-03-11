package com.example.techcentral.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)

public enum OrderStatus {
    PROCESSING, SHIPPING ,FINISHED;
}

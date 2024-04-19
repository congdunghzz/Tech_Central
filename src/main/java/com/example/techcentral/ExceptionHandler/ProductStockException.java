package com.example.techcentral.ExceptionHandler;

public class ProductStockException extends RuntimeException{
    public ProductStockException(String message){
        super(message);
    }
}

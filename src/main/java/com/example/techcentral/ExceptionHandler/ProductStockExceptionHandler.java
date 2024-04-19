package com.example.techcentral.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductStockExceptionHandler {
    @ExceptionHandler(ProductStockException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(ProductStockException e){
        int statusCode = HttpStatus.BAD_REQUEST.value();
        ErrorResponse response = new ErrorResponse(e.getMessage(), statusCode);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

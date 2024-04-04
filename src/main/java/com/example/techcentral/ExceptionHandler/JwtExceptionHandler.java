package com.example.techcentral.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler {
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(NotFoundException e){
        int statusCode = HttpStatus.UNAUTHORIZED.value();
        ErrorResponse response = new ErrorResponse(e.getMessage(), statusCode);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}

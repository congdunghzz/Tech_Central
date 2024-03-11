package com.example.techcentral.ExceptionHandler;

import com.example.techcentral.ExceptionHandler.ErrorResponse;
import com.example.techcentral.ExceptionHandler.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(NotFoundException e){
        int statusCode = HttpStatus.NOT_FOUND.value();
        ErrorResponse response = new ErrorResponse(e.getMessage(), statusCode);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

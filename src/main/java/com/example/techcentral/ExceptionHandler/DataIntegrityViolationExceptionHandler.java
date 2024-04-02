package com.example.techcentral.ExceptionHandler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DataIntegrityViolationExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handle (DataIntegrityViolationException exception){
        int statusCode = HttpStatus.BAD_REQUEST.value();
        ErrorResponse response = new ErrorResponse(exception.getMessage(), statusCode);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

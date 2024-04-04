package com.example.techcentral.ExceptionHandler;

public class JwtException extends RuntimeException{

    public JwtException(String message){
        super(message);
    }
}

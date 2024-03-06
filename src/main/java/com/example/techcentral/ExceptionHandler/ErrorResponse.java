package com.example.techcentral.ExceptionHandler;

public record ErrorResponse(
        String message,
        int statusCode
) {
}

package com.example.techcentral.dto.user.Login;

public record LoginRequest(
        String userEmail,
        String password
) {
}

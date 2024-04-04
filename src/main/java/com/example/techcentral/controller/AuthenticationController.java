package com.example.techcentral.controller;

import com.example.techcentral.dto.user.Login.AuthenticationResponse;
import com.example.techcentral.dto.user.Login.LoginRequest;
import com.example.techcentral.dto.user.UserDTO;
import com.example.techcentral.dto.user.UserRegisterRequest;
import com.example.techcentral.service.AuthenticationService;
import com.example.techcentral.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerNewUser (@RequestBody UserRegisterRequest request){

        boolean response = authenticationService.createUser(request);
        Map<String, String> result = new HashMap<>();
        if (response) {
            result.put("message", "ok");
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }else {
            result.put("message", "error");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody LoginRequest request){

        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

}

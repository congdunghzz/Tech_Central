package com.example.techcentral.service;

import com.example.techcentral.ExceptionHandler.ExistException;
import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.example.techcentral.config.jwtConfig.JwtTokenProvider;
import com.example.techcentral.dao.UserRepository;
import com.example.techcentral.dto.user.CustomUserDetail;
import com.example.techcentral.dto.user.Login.AuthenticationResponse;
import com.example.techcentral.dto.user.Login.LoginRequest;
import com.example.techcentral.dto.user.UserRegisterRequest;
import com.example.techcentral.enums.UserRole;
import com.example.techcentral.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthenticationResponse createUser(UserRegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new ExistException("user with email: " + request.getEmail()+" have existed");
        }
        User createdUser;
        User user = User
                .builder()
                .name(request.getName())
                .dob(request.getDob())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_USER)
                .build();


            createdUser = userRepository.save(user);

            if (createdUser != null){

                String token = jwtTokenProvider.generateToken(new CustomUserDetail(createdUser));
                return new AuthenticationResponse(token, createdUser.getRole().name());
            }else {
                throw new NotFoundException("Sign up falsely");
            }

    }

    public AuthenticationResponse login (LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.userEmail(),
                        request.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
        return new AuthenticationResponse(token, ((CustomUserDetail) authentication.getPrincipal()).getUser().getRole().name());
    }

}

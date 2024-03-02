package com.example.techcentral.config;

import com.example.techcentral.dto.user.CustomUserDetail;
import com.example.techcentral.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        boolean isAuthenticated = false;

        Long userId = 0L;
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")){
                    userId = Long.valueOf(cookie.getValue());
                    isAuthenticated = true;
                    break;
                }
            }
        }
        if (userId != 0 && isAuthenticated){
            UserDetails userDetail = (userService.loadUserById(userId));
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail,
                    null,
                    userDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }
}

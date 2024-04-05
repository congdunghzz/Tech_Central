package com.example.techcentral.config.jwtConfig;

import com.example.techcentral.ExceptionHandler.JwtException;
import com.example.techcentral.dto.user.CustomUserDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String KEY = "fCeDw+hIGGCwSVFo0Y883e3iko8s4huUSqDswc8mXk6xFyTnof0ihw==";
    private static final long EXPIRATION = 1000L * 60 * 60 * 24 * 60;


    public <T> T extractClaim (String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String generateToken(CustomUserDetail userDetail){
        return generateToken(new HashMap<>(), userDetail);
    }
    public String generateToken(Map<String, Object> extractClaim, CustomUserDetail userDetails){
        return Jwts.builder()
                .setClaims(extractClaim)
                .setSubject(Long.toString(userDetails.getUser().getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId (String token){
        String userId = extractClaim(token, Claims::getSubject);
        return Long.parseLong(userId);
    }

    public boolean validateToken(String token) {
        String userId = extractClaim(token, Claims::getSubject);
        return (userId != null && !IsTokenExpired(token));
    }

    public boolean IsTokenExpired (String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Key getSignInKey(){
        byte[] key = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(key);
    }
}

package com.alten.kata.config;


import com.alten.kata.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

    @Component
    public class JwtUtil {

        @Value("${application.security.jwt.secret}")
        private String secret;
        @Value("${application.security.jwt.expiration}")
        private long expiration;

        public String generateToken(User user) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("email", user.getEmail());
            claims.put("authorities", user.getAuthorities());
            claims.put("password", user.getPassword());
            return createToken(claims, user.getEmail());
        }



        private String createToken(Map<String, Object> claims, String username) {
            return Jwts.builder()
                    .claims(claims)
                    .subject(username)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey())
                    .compact();
        }

        private Key getSigningKey() {
            byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        }
        public String getUserNameFromJwtToken(String token) {
            return Jwts.builder().signWith(getSigningKey())
                    .parseClaimsJws(token).getBody().getSubject();
        }


    }




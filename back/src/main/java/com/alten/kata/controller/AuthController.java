package com.alten.kata.controller;

import com.alten.kata.config.JwtUtil;
import com.alten.kata.dto.LoginRequest;
import com.alten.kata.dto.RegisterDTO;
import com.alten.kata.entity.User;
import com.alten.kata.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody  @Valid RegisterDTO request, BindingResult result) {
        return ResponseEntity.ok(userDetailsService.save(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            var userDetails = userDetailsService.loadUserByUsername(request.getEmail());
           /* if (userDetails != null && new BCryptPasswordEncoder().matches(request.getPassword(), userDetails.getPassword())) {
                var token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(token);
            }*/
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
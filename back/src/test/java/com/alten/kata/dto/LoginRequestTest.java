package com.alten.kata.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class LoginRequestTest  {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidloginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("Youssef.ftaich@gmail.com");
        loginRequest.setPassword("myPasswordAlten");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertTrue(violations.isEmpty(), "Should be valid");
    }

    @Test
    public void testInPasswordValid() {
        var loginRequest = new LoginRequest();

        loginRequest.setEmail("Youssef.ftaich@gmail.com");
        loginRequest.setPassword(null);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty(), "Should be invalid due to empty password");
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidEmail() {
        var loginRequest = new LoginRequest();
        loginRequest.setEmail("invalidEmailFormat"); // Invalid email
        loginRequest.setPassword("myPasswordAlten");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertFalse(violations.isEmpty(), "Should be invalid due to invalid email format");
        assertEquals(1, violations.size());
    }
}
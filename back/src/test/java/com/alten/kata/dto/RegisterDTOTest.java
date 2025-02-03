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
public class  RegisterDTOTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidRegisterDTO() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("ucef");
        registerDTO.setFirstname("Youssef");
        registerDTO.setEmail("Youssef.ftaich@gmail.com");
        registerDTO.setPassword("myPasswordAlten");

        Set<ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertTrue(violations.isEmpty(), "Should be valid");
    }

    @Test
    public void testInvalidUsername() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(""); // Invalid (Empty)
        registerDTO.setFirstname("Youssef");
        registerDTO.setEmail("Youssef.ftaich@gmail.com");
        registerDTO.setPassword("myPasswordAlten");

        Set<ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertFalse(violations.isEmpty(), "Should be invalid due to empty username");
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidEmail() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("ucef");
        registerDTO.setFirstname("Youssef");
        registerDTO.setEmail("invalidEmailFormat"); // Invalid email
        registerDTO.setPassword("myPasswordAlten");

        Set<ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertFalse(violations.isEmpty(), "Should be invalid due to invalid email format");
        assertEquals(1, violations.size());
    }

    @Test
    public void testInmyPasswordAlten() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("ucef");
        registerDTO.setFirstname("Youssef");
        registerDTO.setEmail("Youssef.ftaich@gmail.com");
        registerDTO.setPassword("123"); // Less than 6 characters

        Set<ConstraintViolation<RegisterDTO>> violations = validator.validate(registerDTO);
        assertFalse(violations.isEmpty(), "Should be invalid due to password less than 6 characters");
        assertEquals(1, violations.size());
    }
}
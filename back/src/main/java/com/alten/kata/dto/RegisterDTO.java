package com.alten.kata.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterDTO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String firstname;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6,message = "Minimum Password length is 6 characters  ")
    private String password;
}
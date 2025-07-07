package com.example.auth_service.dto;

import com.example.auth_service.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @Size(min=4, max=20, message="Username must be between 4 and 20 characters")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Size(min=8, message = "Password must be atleast 8 characters long")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Role must be specified")
    private Role role;
}

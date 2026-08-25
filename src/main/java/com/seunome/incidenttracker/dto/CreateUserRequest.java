package com.seunome.incidenttracker.dto;

import com.seunome.incidenttracker.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateUserRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8, message = "A senha precisa ter pelo menos 8 caracteres")
        String password,
    @NotNull RoleName role,
    UUID teamId) {}

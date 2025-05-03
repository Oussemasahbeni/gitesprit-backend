package com.esprit.gitesprit.users.infrastructure.dto.request;

import com.esprit.gitesprit.auth.domain.enums.RoleType;
import com.esprit.gitesprit.shared.validators.NotEmptyCollection;
import jakarta.validation.constraints.*;

import java.util.List;

public record UserRequestDto(
        @Email(message = "Invalid email address")
        @NotNull(message = "Email is required")
        @NotBlank(message = "Email is required")
        String email,
        @NotNull(message = "First name is required")
        @NotBlank(message = "First name is required")
        @Size(min = 2, message = "First name must be at least 2 characters long")
        String firstName,
        @NotNull(message = "Username is required")
        @NotBlank(message = "Username is required")
        @Size(min = 2, message = "Username must be at least 2 characters long")
        String username,
        @NotNull(message = "Last name is required")
        @NotBlank(message = "Last name is required")
        @Size(min = 2, message = "Last name must be at least 2 characters long")
        String lastName,
        @NotNull(message = "Phone number is required")
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "\\d{8}", message = "Phone number must be exactly 8 digits")
        String phoneNumber,
        String address,
        @NotEmptyCollection(message = "Roles are required") List<RoleType> roles
        ) {}

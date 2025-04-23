package com.esprit.gitesprit.auth.infra.dto.request;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
        @NotBlank String id,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 2, max = 50) String firstName,
        @NotBlank @Size(min = 2, max = 50) String lastName,
        @NotNull Boolean enabled,
        Locale locale,
        String phoneNumber,
        String profilePicture,
        @NotNull Boolean emailVerified) {}

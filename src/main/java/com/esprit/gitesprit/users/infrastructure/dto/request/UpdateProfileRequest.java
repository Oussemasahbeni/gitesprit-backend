package com.esprit.gitesprit.users.infrastructure.dto.request;

import com.esprit.gitesprit.users.domain.enums.Gender;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateProfileRequest(
        UUID id,
        String firstName,
        String lastName,
        @Nullable String phoneNumber,
        @Nullable LocalDate dateOfBirth,
        @Nullable Gender gender) {}

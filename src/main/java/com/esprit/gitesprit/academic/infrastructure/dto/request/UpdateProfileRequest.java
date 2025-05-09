package com.esprit.gitesprit.academic.infrastructure.dto.request;

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

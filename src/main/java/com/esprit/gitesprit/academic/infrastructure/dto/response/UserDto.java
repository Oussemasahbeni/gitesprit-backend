package com.esprit.gitesprit.academic.infrastructure.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String fullName,
        String firstName,
        String username,
        String lastName,
        String profilePicture,
        String phoneNumber,
        LocalDate birthDate,
        Gender gender,
        Instant createdAt,
        Instant updatedAt,
        short version) {}

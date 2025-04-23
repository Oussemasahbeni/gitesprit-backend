package com.esprit.gitesprit.users.infrastructure.dto.response;

import com.esprit.gitesprit.users.domain.enums.Gender;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String fullName,
        String firstName,
        String lastName,
        String profilePicture,
        String phoneNumber,
        LocalDate birthDate,
        Gender gender,
        Instant createdAt,
        Instant updatedAt,
        short version) {}

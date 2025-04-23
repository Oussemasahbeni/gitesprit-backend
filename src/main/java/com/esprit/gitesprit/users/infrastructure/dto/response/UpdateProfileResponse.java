package com.esprit.gitesprit.users.infrastructure.dto.response;

import com.esprit.gitesprit.users.domain.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateProfileResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address,
        LocalDate dateOfBirth,
        Gender gender) {}

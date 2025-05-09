package com.esprit.gitesprit.academic.infrastructure.dto.response;

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

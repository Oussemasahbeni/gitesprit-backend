package com.esprit.gitesprit.users.infrastructure.dto.request;

import com.esprit.gitesprit.users.domain.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record UploadProfileRequest(
        UUID id,
        LocalDate birthDate,
        Gender gender) {
}

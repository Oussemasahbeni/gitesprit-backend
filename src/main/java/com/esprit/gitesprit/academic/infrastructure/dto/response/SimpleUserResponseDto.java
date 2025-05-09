package com.esprit.gitesprit.academic.infrastructure.dto.response;

import java.util.UUID;

public record SimpleUserResponseDto(
        UUID id, String fullName, String firstName, String lastName, String profilePicture) {}

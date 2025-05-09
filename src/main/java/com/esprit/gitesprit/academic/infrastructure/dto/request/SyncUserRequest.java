package com.esprit.gitesprit.academic.infrastructure.dto.request;

import java.util.List;
import java.util.UUID;

public record SyncUserRequest(
         UUID id,
            String email,
            String username,
            String firstName,
            String lastName,
            List<String> roles,
            boolean enabled,
            boolean emailVerified
) {
}

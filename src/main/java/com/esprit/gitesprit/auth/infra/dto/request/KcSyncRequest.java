package com.esprit.gitesprit.auth.infra.dto.request;

import com.esprit.gitesprit.auth.domain.enums.SyncActions;
import jakarta.annotation.Nullable;

public record KcSyncRequest(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled,
        boolean emailVerified,
        @Nullable String phoneNumber,
        @Nullable String profilePicture,
        //    List<String> roles,
        //    List<String> groups,
        SyncActions action) {
}

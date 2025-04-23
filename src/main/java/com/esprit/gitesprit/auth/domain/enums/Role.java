package com.esprit.gitesprit.auth.domain.enums;

import java.util.Optional;

public enum Role {
    super_admin,
    admin,
    user,
    partner;

    public static Optional<Role> fromString(String roleStr) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleStr)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }
}

package com.esprit.gitesprit.auth.domain.enums;

import java.util.Optional;

public enum RoleType {
    admin,
    teacher,
    student;

    public static Optional<RoleType> fromString(String roleStr) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.name().equalsIgnoreCase(roleStr)) {
                return Optional.of(roleType);
            }
        }
        return Optional.empty();
    }
}

package com.esprit.gitesprit.auth.domain.model;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.RoleType;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private List<RoleType> roleTypes;
    private Boolean enabled;
    private Boolean emailVerified;
    private Instant createdAt;
    private Map<String, List<String>> attributes;

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}

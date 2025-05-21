package com.esprit.gitesprit.git.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributorInfo {
    private String name;
    private String email;
    private long commitCount;

    // Override equals and hashCode for proper Set behavior
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContributorInfo that = (ContributorInfo) o;
        return Objects.equals(email, that.email); // Use email for uniqueness
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
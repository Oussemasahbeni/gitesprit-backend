package com.esprit.gitesprit.users.infrastructure.entity;

import com.esprit.gitesprit.auth.domain.enums.Role;
import com.esprit.gitesprit.users.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "user_roles")
public class RoleEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role name;
}

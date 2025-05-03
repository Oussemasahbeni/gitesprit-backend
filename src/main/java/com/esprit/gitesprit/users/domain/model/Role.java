package com.esprit.gitesprit.users.domain.model;

import com.esprit.gitesprit.auth.domain.enums.RoleType;
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
public class Role {

    private UUID id;
    private User user;
    private RoleType name;
}

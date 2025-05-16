package com.esprit.gitesprit.users.infrastructure.repository;

import com.esprit.gitesprit.auth.domain.enums.RoleType;
import com.esprit.gitesprit.users.domain.model.Role;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.name = :roleType")
    List<UserEntity> findAllByRoleType(@Param("roleType") RoleType roleType);

}

package com.esprit.gitesprit.academic.infrastructure.repository;

import com.esprit.gitesprit.academic.infrastructure.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<GroupEntity, UUID>, JpaSpecificationExecutor<GroupEntity> {}

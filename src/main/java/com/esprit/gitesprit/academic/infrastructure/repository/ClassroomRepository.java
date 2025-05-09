package com.esprit.gitesprit.academic.infrastructure.repository;

import com.esprit.gitesprit.academic.infrastructure.entity.ClassroomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<ClassroomEntity, UUID>, JpaSpecificationExecutor<ClassroomEntity> {}


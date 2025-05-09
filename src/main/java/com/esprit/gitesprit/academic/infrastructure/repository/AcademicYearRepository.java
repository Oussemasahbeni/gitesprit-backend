package com.esprit.gitesprit.academic.infrastructure.repository;

import com.esprit.gitesprit.academic.infrastructure.entity.AcademicYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AcademicYearRepository extends JpaRepository<AcademicYearEntity, UUID>, JpaSpecificationExecutor<AcademicYearEntity> {}

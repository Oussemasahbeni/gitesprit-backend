package com.esprit.gitesprit.academic.infrastructure.repository;

import com.esprit.gitesprit.academic.infrastructure.entity.AcademicYearEntity;
import com.esprit.gitesprit.academic.infrastructure.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID>, JpaSpecificationExecutor<TaskEntity> {
    List<TaskEntity> findAllByGroupStudentId(UUID groupStudentId);
}

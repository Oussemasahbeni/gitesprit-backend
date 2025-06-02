package com.esprit.gitesprit.academic.infrastructure.repository;

import com.esprit.gitesprit.academic.infrastructure.entity.GroupStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupStudentRepository extends JpaRepository<GroupStudentEntity, UUID> {
    List<GroupStudentEntity> findByGroupId(UUID groupId);
    List<GroupStudentEntity> findByStudentId(UUID studentId);
    Optional<GroupStudentEntity> findByGroupIdAndStudentId(UUID groupId, UUID studentId);
}

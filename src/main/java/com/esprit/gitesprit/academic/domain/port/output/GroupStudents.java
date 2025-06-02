package com.esprit.gitesprit.academic.domain.port.output;

import com.esprit.gitesprit.academic.domain.model.GroupStudent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupStudents {
    GroupStudent create(GroupStudent groupStudent);
    GroupStudent update(GroupStudent groupStudent);
    Optional<GroupStudent> findById(UUID id);
    List<GroupStudent> findByGroupId(UUID groupId);
    List<GroupStudent> findByStudentId(UUID studentId);
    Optional<GroupStudent> findByGroupIdAndStudentId(UUID groupId, UUID studentId);
    void deleteById(UUID id);
}

package com.esprit.gitesprit.academic.domain.port.input;

import com.esprit.gitesprit.academic.domain.model.GroupStudent;

import java.util.List;
import java.util.UUID;

public interface GroupStudentUseCases {
    GroupStudent create(GroupStudent groupStudent);
    GroupStudent update(GroupStudent groupStudent);
    GroupStudent findById(UUID id);
    List<GroupStudent> findByGroupId(UUID groupId);
    List<GroupStudent> findByStudentId(UUID studentId);
    GroupStudent findByGroupIdAndStudentId(UUID groupId, UUID studentId);
    GroupStudent addMarkToGroupStudent(UUID groupId, UUID studentId, Double mark, String comment);
    GroupStudent calculateMark(UUID id);
    void deleteById(UUID id);
}

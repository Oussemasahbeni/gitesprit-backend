package com.esprit.gitesprit.academic.domain.port.input;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.users.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GroupUseCases {
    Group create(Group group, UUID subjectId);

    Group update(Group group);

    Group assignStudent(UUID groupId, UUID student);

    Group removeStudent(UUID groupId, UUID student);

    Group assignStudents(UUID groupId, List<UUID> students);

    Group findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Group> findAll();

    List<Group> findAllByStudentId(UUID studentId);

    Page<Group> findAllPaginated(String search, Pageable pageable);
}

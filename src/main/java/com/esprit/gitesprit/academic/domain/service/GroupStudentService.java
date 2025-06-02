package com.esprit.gitesprit.academic.domain.service;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.domain.model.GroupStudent;
import com.esprit.gitesprit.academic.domain.port.input.GroupStudentUseCases;
import com.esprit.gitesprit.academic.domain.port.input.GroupUseCases;
import com.esprit.gitesprit.academic.domain.port.output.GroupStudents;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.DomainService;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.domain.port.output.Users;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class GroupStudentService implements GroupStudentUseCases {
    private final GroupStudents groupStudents;
    private final GroupUseCases groupUseCases;
    private final Users users;

    @Override
    public GroupStudent create(GroupStudent groupStudent) {
        return groupStudents.create(groupStudent);
    }

    @Override
    public GroupStudent update(GroupStudent groupStudent) {
        findById(groupStudent.getId());
        return groupStudents.update(groupStudent);
    }

    @Override
    public GroupStudent findById(UUID id) {
        return groupStudents.findById(id).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.GENERIC_NOT_FOUND, "GroupStudent not found")
        );
    }

    @Override
    public List<GroupStudent> findByGroupId(UUID groupId) {
        return groupStudents.findByGroupId(groupId);
    }

    @Override
    public List<GroupStudent> findByStudentId(UUID studentId) {
        return groupStudents.findByStudentId(studentId);
    }

    @Override
    public GroupStudent findByGroupIdAndStudentId(UUID groupId, UUID studentId) {
        return groupStudents.findByGroupIdAndStudentId(groupId, studentId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.GENERIC_NOT_FOUND, "Student not found in group")
        );
    }

    @Override
    public GroupStudent addMarkToGroupStudent(UUID groupId, UUID studentId, Double mark, String comment) {
        // Find group and student to ensure they exist
        Group group = groupUseCases.findById(groupId);
        User student = users.findById(studentId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND)
        );

        // First check if the group already has this student in its groupStudents collection
        GroupStudent groupStudent = null;
        if (group.getStudents() != null) {
            groupStudent = group.getStudents().stream()
                    .filter(gs -> gs.getStudent().getId().equals(studentId))
                    .findFirst()
                    .orElse(null);
        }

        // If not found in the group's collection, try to find from the repository or create a new one
        if (groupStudent == null) {
            try {
                groupStudent = findByGroupIdAndStudentId(groupId, studentId);
            } catch (NotFoundException e) {
                // Create a new relationship if it doesn't exist
                groupStudent = new GroupStudent();
                groupStudent.setGroup(group);
                groupStudent.setStudent(student);

                // If group's collection is initialized, add the new relationship there as well
                if (group.getStudents() != null) {
                    group.addGroupStudent(groupStudent);
                }
            }
        }

        // Set mark and comment
        groupStudent.setIndividualMark(mark);
        groupStudent.setIndividualComment(comment);

        return groupStudents.update(groupStudent);
    }

    @Override
    public void deleteById(UUID id) {
        findById(id);
        groupStudents.deleteById(id);
    }
}

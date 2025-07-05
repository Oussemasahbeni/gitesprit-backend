package com.esprit.gitesprit.academic.infrastructure.dto.response;

import com.esprit.gitesprit.auth.infra.dto.response.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GroupDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        String name,
        Double mark,
        String comment,
        int nbRepositories,
        List<GroupStudentDto> students,
        SubjectSimpleDto subject
) {

    public record GroupStudentDto(
            UUID id,
            StudentDto student,
            Double individualMark,
            String individualComment,
            Double finalMark,
            List<TaskDto> tasks
    ) {
        public record StudentDto(
                UUID id,
                String firstName,
                String lastName,
                String fullName,
                String username,
                String email
        ){}
    }
}

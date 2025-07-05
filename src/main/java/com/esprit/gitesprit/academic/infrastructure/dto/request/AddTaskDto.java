package com.esprit.gitesprit.academic.infrastructure.dto.request;

import com.esprit.gitesprit.academic.domain.model.GroupStudent;

import java.time.LocalDate;
import java.util.UUID;

public record AddTaskDto(
        String description,
        LocalDate dueDate,
        Double percentage,
        UUID groupStudentId
) {
}

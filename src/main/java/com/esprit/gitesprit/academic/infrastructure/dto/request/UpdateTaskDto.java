package com.esprit.gitesprit.academic.infrastructure.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record UpdateTaskDto(
        LocalDate dueDate,
        Double mark,
        String comment,
        boolean done,
        Double percentage,
        List<String> branchLinks
) {
}

package com.esprit.gitesprit.academic.infrastructure.dto.request;

import java.util.UUID;

public record AddClassroomDto(
    String name,
    UUID academicYearId)
    {}

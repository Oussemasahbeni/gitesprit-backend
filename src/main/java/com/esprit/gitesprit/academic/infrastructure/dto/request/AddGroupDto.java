package com.esprit.gitesprit.academic.infrastructure.dto.request;

import java.util.UUID;

public record AddGroupDto(String name, UUID subjectId) {}

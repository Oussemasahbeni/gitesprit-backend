package com.esprit.gitesprit.academic.infrastructure.dto.request;

import java.io.Serializable;
import java.util.UUID;

public record AddSubjectDto(String name, UUID classroomId, UUID teacherId, Double groupMarkPercentage, Double individualMarkPercentage){}

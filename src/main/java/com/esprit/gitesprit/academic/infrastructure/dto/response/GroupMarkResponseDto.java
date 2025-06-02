package com.esprit.gitesprit.academic.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMarkResponseDto {
    private UUID id;
    private UUID groupId;
    private String groupName;
    private UUID subjectId;
    private String subjectName;
    private Double mark;
    private String comment;
    private Double weightedMark; // The mark weighted by the subject's group mark percentage
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}

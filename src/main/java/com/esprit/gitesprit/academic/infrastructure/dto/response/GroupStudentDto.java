package com.esprit.gitesprit.academic.infrastructure.dto.response;

import com.esprit.gitesprit.users.infrastructure.dto.response.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupStudentDto {
    private UUID id;
    private UserDto student;
    private GroupSimpleDto group;
    private Double individualMark;
    private String individualComment;
    private Double finalMark;
}

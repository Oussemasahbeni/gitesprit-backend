package com.esprit.gitesprit.academic.infrastructure.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMarkDto {
//    @NotNull(message = "Group mark is required")
//    @Min(value = 0, message = "Mark must be at least 0")
//    @Max(value = 20, message = "Mark must be at most 20")
//    private Double mark;

    private String comment;
}

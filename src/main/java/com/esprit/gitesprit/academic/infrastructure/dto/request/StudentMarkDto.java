package com.esprit.gitesprit.academic.infrastructure.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentMarkDto {

    @Schema(description = "The mark assigned to the student (0-20)", example = "15.5")
    @NotNull(message = "Mark is required")
    @DecimalMin(value = "0.0", message = "Mark must be at least 0")
    @DecimalMax(value = "20.0", message = "Mark cannot be greater than 20")
    private Double mark;

    @Schema(description = "Comments on the student's work", example = "Excellent work on the project implementation")
    private String comment;
}

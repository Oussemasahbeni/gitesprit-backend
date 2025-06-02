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
public class MarkConfigurationDto {
    @NotNull(message = "Group mark percentage is required")
    @Min(value = 0, message = "Group mark percentage must be at least 0")
    @Max(value = 100, message = "Group mark percentage must be at most 100")
    private Double groupMarkPercentage;

    @NotNull(message = "Individual mark percentage is required")
    @Min(value = 0, message = "Individual mark percentage must be at least 0")
    @Max(value = 100, message = "Individual mark percentage must be at most 100")
    private Double individualMarkPercentage;
}

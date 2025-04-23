package com.esprit.gitesprit.auth.infra.dto.request;

import com.esprit.gitesprit.shared.validators.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDto(@Email @NotBlank String email, @NotBlank @ValidPassword String password) {}

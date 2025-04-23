package com.esprit.gitesprit.logs.infra.dto;

import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;

import java.time.Instant;
import java.util.UUID;

public record LogEntryDto(
    UUID id,
    Instant createdAt,
    ActionType action,
    String ipAddress,
    String deviceId,
    ModuleType module,
    String message,
    UserDto user) {
  public record UserDto(
      UUID id,
      String firstName,
      String lastName,
      String email,
      String phoneNumber,
      String address,
      String fullName) {}
}

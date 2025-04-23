package com.esprit.gitesprit.logs.domain.service;

import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.logs.domain.model.LogEntry;
import com.esprit.gitesprit.logs.domain.port.input.LogEntryUseCases;
import com.esprit.gitesprit.logs.domain.port.ouput.LogEntries;
import com.esprit.gitesprit.shared.AuthUtils;
import com.esprit.gitesprit.shared.annotation.DomainService;

import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.domain.port.output.Users;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class LogEntryService implements LogEntryUseCases {

  private final LogEntries logEntries;
  private final Users users;

  @Override
  public void create(ActionType actionType, ModuleType moduleType, String message) {
    HttpServletRequest request =
        ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
            .getRequest();

    String ipAddress = request.getHeader("X-Forwarded-For");
    if (ipAddress == null || ipAddress.isEmpty()) {
      ipAddress = request.getRemoteAddr();
    }

    UUID userId = AuthUtils.getCurrentUserId();
    User user = null;
    if (userId != null) {
      user = users.findById(userId).orElse(null);
    }

    String deviceId = request.getHeader("X-Device-Id");

    LogEntry logEntry =
        LogEntry.builder()
            .user(user)
            .action(actionType)
            .module(moduleType)
            .message(message)
            .ipAddress(ipAddress)
            .deviceId(deviceId)
            .build();
    logEntries.create(logEntry);
  }

  @Override
  public void deleteById(UUID id) {
    logEntries.deleteById(id);
  }

  @Override
  public LogEntry getById(UUID id) {
    return logEntries
        .getById(id)
        .orElseThrow(
            () ->
                new NotFoundException(NotFoundException.NotFoundExceptionType.LOG_ENTRY_NOT_FOUND));
  }

  @Override
  public List<LogEntry> getAllLogEntries() {
    return logEntries.getAllLogEntries();
  }

  @Override
  public Page<LogEntry> getPage(
      ActionType actionType, ModuleType moduleType, String message, Pageable pageable) {
    return logEntries.getPage(actionType, moduleType, message, pageable);
  }

  @Override
  public void deleteByIds(List<UUID> ids) {
    for (UUID id : ids) {
      logEntries.deleteById(id);
    }
  }
}

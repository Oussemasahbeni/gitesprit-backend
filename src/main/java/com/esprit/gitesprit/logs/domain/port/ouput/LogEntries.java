package com.esprit.gitesprit.logs.domain.port.ouput;

import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.logs.domain.model.LogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LogEntries {

  void create(LogEntry logEntry);

  void deleteById(UUID id);

  Optional<LogEntry> getById(UUID id);

  List<LogEntry> getAllLogEntries();

  Page<LogEntry> getPage(
      ActionType actionType, ModuleType moduleType, String message, Pageable pageable);
}

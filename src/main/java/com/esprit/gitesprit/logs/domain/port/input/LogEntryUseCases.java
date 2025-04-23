package com.esprit.gitesprit.logs.domain.port.input;

import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.logs.domain.model.LogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LogEntryUseCases {

  void create(ActionType actionType, ModuleType moduleType, String message);

  void deleteById(UUID id);

  LogEntry getById(UUID id);

  List<LogEntry> getAllLogEntries();

  Page<LogEntry> getPage(
      ActionType actionType, ModuleType moduleType, String message, Pageable pageable);

  void deleteByIds(List<UUID> ids);
}

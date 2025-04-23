package com.esprit.gitesprit.logs.infra.adapter.persistence;

import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.logs.domain.model.LogEntry;
import com.esprit.gitesprit.logs.domain.port.ouput.LogEntries;
import com.esprit.gitesprit.logs.infra.adapter.specification.LogEntrySpec;
import com.esprit.gitesprit.logs.infra.entity.LogEntryEntity;
import com.esprit.gitesprit.logs.infra.mapper.LogEntryMapper;
import com.esprit.gitesprit.logs.infra.repository.LogEntryRepository;
import com.esprit.gitesprit.shared.annotation.PersistenceAdapter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class LogEntryJpaAdapter implements LogEntries {

  private final LogEntryMapper mapper;
  private final LogEntryRepository repository;

  @Override
  public void create(LogEntry logEntry) {
    LogEntryEntity mappedLogEntry = mapper.toEntity(logEntry);
    repository.save(mappedLogEntry);
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }

  @Override
  public Optional<LogEntry> getById(UUID id) {
    return repository.findById(id).map(mapper::toModel);
  }

  @Override
  public List<LogEntry> getAllLogEntries() {
    return repository.findAll().stream().map(mapper::toModel).toList();
  }

  @Override
  public Page<LogEntry> getPage(
      ActionType actionType, ModuleType moduleType, String searchTerm, Pageable pageable) {
    Specification<LogEntryEntity> spec =
        LogEntrySpec.hasCondition(actionType, moduleType, searchTerm);
    return repository.findAll(spec, pageable).map(mapper::toModel);
  }
}

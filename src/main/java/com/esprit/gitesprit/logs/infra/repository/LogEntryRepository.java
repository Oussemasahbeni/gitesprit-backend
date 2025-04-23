package com.esprit.gitesprit.logs.infra.repository;

import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.logs.infra.entity.LogEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface LogEntryRepository
    extends JpaRepository<LogEntryEntity, UUID>, JpaSpecificationExecutor<LogEntryEntity> {
  List<LogEntryEntity> findByModule(ModuleType moduleType);
}

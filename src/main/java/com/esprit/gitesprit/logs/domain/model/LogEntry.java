package com.esprit.gitesprit.logs.domain.model;

import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.shared.AbstractAuditingModel;
import com.esprit.gitesprit.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LogEntry extends AbstractAuditingModel {

  private UUID id;
  private User user;
  private ActionType action;
  private String deviceId;
  private String ipAddress;
  private ModuleType module;
  private String message;
}

package com.esprit.gitesprit.logs.infra.entity;

import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.shared.AbstractAuditingEntity;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import jakarta.persistence.*;
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
@Entity
@Table(
    name = "log_entries",
    indexes = {
      @Index(name = "idx_user", columnList = "user_id"),
      @Index(name = "idx_action", columnList = "action"),
      @Index(name = "idx_module", columnList = "module")
    })
public class LogEntryEntity extends AbstractAuditingEntity {

  @Id @GeneratedValue private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ActionType action;

  @Column(name = "device_id")
  private String deviceId;

  @Column(name = "ip_address", nullable = false)
  private String ipAddress;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ModuleType module;

  @Column(columnDefinition = "TEXT")
  private String message;
}

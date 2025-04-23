package com.esprit.gitesprit.logs.infra.adapter.specification;

import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.logs.infra.entity.LogEntryEntity;
import org.springframework.data.jpa.domain.Specification;

public class LogEntrySpec {

  private LogEntrySpec() {}

  public static Specification<LogEntryEntity> hasCondition(
      ActionType actionType, ModuleType moduleType, String searchTerm) {
    return (root, query, criteriaBuilder) -> {
      var predicates = criteriaBuilder.conjunction();

      if (actionType != null) {
        predicates =
            criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("action"), actionType));
      }
      if (moduleType != null) {
        predicates =
            criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("module"), moduleType));
      }
      if (searchTerm != null && !searchTerm.isEmpty()) {
        predicates =
            criteriaBuilder.and(
                predicates,
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("message")),
                    "%" + searchTerm.toLowerCase() + "%"));
      }

      return predicates;
    };
  }
}

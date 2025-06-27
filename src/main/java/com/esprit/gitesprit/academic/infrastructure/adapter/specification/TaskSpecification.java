package com.esprit.gitesprit.academic.infrastructure.adapter.specification;

import com.esprit.gitesprit.academic.infrastructure.entity.SubjectEntity;
import com.esprit.gitesprit.academic.infrastructure.entity.TaskEntity;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    private TaskSpecification() {}

    public static Specification<TaskEntity> hasCriteria(String criteria) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + criteria.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")), likePattern));
        };
    }
}

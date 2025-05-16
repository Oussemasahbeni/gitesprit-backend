package com.esprit.gitesprit.academic.infrastructure.adapter.specification;

import com.esprit.gitesprit.academic.infrastructure.entity.GroupEntity;
import com.esprit.gitesprit.academic.infrastructure.entity.SubjectEntity;
import org.springframework.data.jpa.domain.Specification;

public class SubjectSpecification {
    private SubjectSpecification(){}

    public static Specification<SubjectEntity> hasCriteria(String criteria) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + criteria.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
        };
    }
}

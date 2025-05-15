package com.esprit.gitesprit.academic.infrastructure.adapter.specification;

import com.esprit.gitesprit.academic.infrastructure.entity.ClassroomEntity;
import org.springframework.data.jpa.domain.Specification;

public class ClassroomSpecification {

    private ClassroomSpecification() {}

    public static Specification<ClassroomEntity> hasCriteria(String criteria) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + criteria.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
        };
    }
}

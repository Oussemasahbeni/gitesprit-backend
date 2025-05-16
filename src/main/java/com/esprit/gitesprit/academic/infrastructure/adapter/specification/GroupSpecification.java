package com.esprit.gitesprit.academic.infrastructure.adapter.specification;

import com.esprit.gitesprit.academic.infrastructure.entity.ClassroomEntity;
import com.esprit.gitesprit.academic.infrastructure.entity.GroupEntity;
import org.springframework.data.jpa.domain.Specification;

public class GroupSpecification {

    private GroupSpecification(){}

    public static Specification<GroupEntity> hasCriteria(String criteria) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + criteria.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
        };
    }
}

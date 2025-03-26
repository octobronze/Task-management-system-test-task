package com.example.Task_management_system_test_task.specifications;

import com.example.Task_management_system_test_task.enums.entity_fetch_fields.UserFetchFields;
import com.example.Task_management_system_test_task.tables.User;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Builder
@Getter
public class UserSpecification implements Specification<User> {
    private static final String ID = "id";
    private static final String EMAIl = "email";

    private Integer id;
    private String email;
    private List<UserFetchFields> fetchFields;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and();

        if (getId() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(ID), getId()));
        }
        if (getEmail() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(EMAIl), getEmail()));
        }

        for (var fetchField : fetchFields) {
            root.fetch(fetchField.getField(), JoinType.LEFT);
        }

        return predicate;
    }
}

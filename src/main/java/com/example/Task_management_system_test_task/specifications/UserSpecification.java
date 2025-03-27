package com.example.Task_management_system_test_task.specifications;

import com.example.Task_management_system_test_task.tables.User;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Builder
@Getter
public class UserSpecification implements Specification<User> {
    private static final String ID = "id";
    private static final String EMAIl = "email";

    private FetchService<User> fetchService;
    private Integer id;
    private String email;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and();

        if (id != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(ID), getId()));
        }
        if (email != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(EMAIl), getEmail()));
        }

        if (fetchService != null) {
            fetchService.fetch(root);
        }

        return predicate;
    }
}

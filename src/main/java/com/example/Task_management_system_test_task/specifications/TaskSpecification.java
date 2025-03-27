package com.example.Task_management_system_test_task.specifications;

import com.example.Task_management_system_test_task.tables.Task;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Builder
@Getter
public class TaskSpecification implements Specification<Task> {
    private static final String IMPLEMENTER = "implementer";
    private static final String CREATOR = "creator";
    private static final String ID = "id";

    private Integer id;
    private Integer implementerId;
    private Integer creatorId;
    private FetchService<Task> fetchService;

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and();

        if (id != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(ID), id));
        }
        if (creatorId != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(CREATOR).get(ID), creatorId));
        }
        if (implementerId != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(IMPLEMENTER).get(ID), implementerId));
        }

        if (fetchService != null) {
            fetchService.fetch(root);
        }

        return predicate;
    }
}

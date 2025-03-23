package com.example.Task_management_system_test_task.specifications;

import com.example.Task_management_system_test_task.dtos.TaskFilterRequestDto;
import com.example.Task_management_system_test_task.tables.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification extends TaskFilterRequestDto implements Specification<Task> {
    private static final String IMPLEMENTER = "implementer";
    private static final String CREATOR = "creator";
    private static final String ID = "id";

    public TaskSpecification(TaskFilterRequestDto taskFilterRequestDto) {
        setImplementerId(taskFilterRequestDto.getImplementerId());
        setCreatorId(taskFilterRequestDto.getCreatorId());
    }

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (getCreatorId() != null) {
            predicates.add(criteriaBuilder.equal(root.get(CREATOR).get(ID), getCreatorId()));
        }
        if (getImplementerId() != null) {
            predicates.add(criteriaBuilder.equal(root.get(IMPLEMENTER).get(ID), getImplementerId()));
        }

        return compoundPredicates(predicates, criteriaBuilder);
    }

    private Predicate compoundPredicates(List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
        Predicate response = criteriaBuilder.and();

        for (Predicate predicate : predicates) {
            response = criteriaBuilder.and(predicate);
        }

        return response;
    }
}

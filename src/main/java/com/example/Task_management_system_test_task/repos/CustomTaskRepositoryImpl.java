package com.example.Task_management_system_test_task.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public class CustomTaskRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomTaskRepository<T, ID> {
    private static final String TASK_COMMENTS_ENTITY_GRAPH = "task.comments";

    private final EntityManager entityManager;
    private final Class<T> domainClass;

    public CustomTaskRepositoryImpl(JpaEntityInformation<T,?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
    }

    @Override
    public Page<T> findAllWithComments(Specification<T> spec, Pageable pageable) {
        TypedQuery<T> query = getQuery(spec, pageable);
        query.setHint(EntityGraph.EntityGraphType.LOAD.getKey(), entityManager.getEntityGraph(TASK_COMMENTS_ENTITY_GRAPH));

        return readPage(query, domainClass, pageable, spec);
    }
}

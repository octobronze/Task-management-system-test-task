package com.example.Task_management_system_test_task.repos;

import com.example.Task_management_system_test_task.tables.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @EntityGraph(attributePaths = {"comments"})
    @Query("select t from Task t where t.creator.id = :creatorId")
    List<Task> findByCreatorIdWithComments(@Param("creatorId") Integer creatorId);

    @EntityGraph(attributePaths = {"comments"})
    @Query("select t from Task t where t.implementer.id = :implementerId")
    List<Task> findByImplementerIdWithComments(@Param("implementerId") Integer implementerId);
}

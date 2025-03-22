package com.example.Task_management_system_test_task.repos;

import com.example.Task_management_system_test_task.tables.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}

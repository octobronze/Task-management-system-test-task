package com.example.Task_management_system_test_task.repos;

import com.example.Task_management_system_test_task.tables.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}

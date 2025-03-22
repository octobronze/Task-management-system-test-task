package com.example.Task_management_system_test_task.repos;

import com.example.Task_management_system_test_task.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}

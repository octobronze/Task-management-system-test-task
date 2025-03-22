package com.example.Task_management_system_test_task.controllers;

import com.example.Task_management_system_test_task.dtos.TaskCreateRequestDto;
import com.example.Task_management_system_test_task.security.UserDetailsImpl;
import com.example.Task_management_system_test_task.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @PostMapping
    public ResponseEntity<String> createTask(Authentication authentication, @RequestBody TaskCreateRequestDto requestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();
        taskService.createTask(userDetails.getId(), requestDto);

        return ResponseEntity.ok("ok");
    }
}

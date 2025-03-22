package com.example.Task_management_system_test_task.controllers;

import com.example.Task_management_system_test_task.dtos.TaskCreateRequestDto;
import com.example.Task_management_system_test_task.dtos.TaskGetResponseDto;
import com.example.Task_management_system_test_task.dtos.TaskUpdateByAdminRequestDto;
import com.example.Task_management_system_test_task.dtos.TaskUpdateRequestDto;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.security.UserDetailsImpl;
import com.example.Task_management_system_test_task.services.AuthService;
import com.example.Task_management_system_test_task.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.ACCESS_DENIED;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<String> createTask(Authentication authentication, @RequestBody TaskCreateRequestDto requestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();

        taskService.createTask(userDetails.getId(), requestDto);

        return ResponseEntity.ok("ok");
    }

    @PutMapping
    public ResponseEntity<String> updateTask(Authentication authentication, @RequestBody TaskUpdateRequestDto requestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();

        if (!authService.hasAccessToUpdateTask(userDetails, requestDto.getId())) {
            throw new BadRequestException(ACCESS_DENIED);
        }

        taskService.updateTask(requestDto, userDetails.getId());

        return ResponseEntity.ok("ok");
    }

    @PutMapping("/admin")
    public ResponseEntity<String> updateTask(@RequestBody TaskUpdateByAdminRequestDto requestDto) {
        taskService.updateTask(requestDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable(name = "id") Integer id) {
        taskService.deleteTask(id);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/creator/{id}")
    public ResponseEntity<List<TaskGetResponseDto>> getTaskByCreatorId(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(taskService.getTasksByCreatorId(id));
    }

    @GetMapping("/implementer/{id}")
    public ResponseEntity<List<TaskGetResponseDto>> getTaskByImplementerId(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(taskService.getTaskByImplementerId(id));
    }
}

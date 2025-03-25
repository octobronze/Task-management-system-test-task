package com.example.Task_management_system_test_task.controllers;

import com.example.Task_management_system_test_task.dtos.*;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.security.UserPrincipal;
import com.example.Task_management_system_test_task.services.AuthService;
import com.example.Task_management_system_test_task.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.ACCESS_DENIED;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final AuthService authService;

    @PostMapping
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Добавление задачи")
    public ResponseEntity<String> createTask(Authentication authentication, @RequestBody TaskCreateRequestDto requestDto) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        taskService.createTask(userPrincipal.getId(), requestDto);

        return ResponseEntity.ok("ok");
    }

    @PutMapping
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Изменение задачи обычным пользователем")
    public ResponseEntity<String> updateTask(Authentication authentication, @RequestBody TaskUpdateRequestDto requestDto) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (!authService.hasAccessToUpdateTask(userPrincipal, requestDto.getId())) {
            throw new BadRequestException(ACCESS_DENIED);
        }

        taskService.updateTask(requestDto);

        return ResponseEntity.ok("ok");
    }

    @PutMapping("/admin")
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Изменение задачи администратором")
    public ResponseEntity<String> updateTask(@RequestBody TaskUpdateByAdminRequestDto requestDto) {
        taskService.updateTask(requestDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Удаление задачи")
    public ResponseEntity<String> deleteTask(@PathVariable(name = "id") Integer id) {
        taskService.deleteTask(id);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/filters")
    @SecurityRequirement(name = "JWT")
    @Operation(description = "Получение задач с использованием фильтрации и пагинации")
    public ResponseEntity<Page<TaskGetResponseDto>> getTasksWithFilters(@RequestBody TaskFilterRequestDto requestDto) {
        return ResponseEntity.ok(taskService.getTasksWithFilters(requestDto));
    }
}

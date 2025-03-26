package com.example.Task_management_system_test_task.controllers;

import com.example.Task_management_system_test_task.dtos.CommentCreateRequestDto;
import com.example.Task_management_system_test_task.security.UserPrincipal;
import com.example.Task_management_system_test_task.services.AuthService;
import com.example.Task_management_system_test_task.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.ACCESS_DENIED;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final AuthService authService;

    @PostMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление комментария")
    public ResponseEntity<String> createComment(Authentication authentication,
                                                @RequestBody CommentCreateRequestDto requestDto) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (!authService.hasAccessToCreateComment(userPrincipal, requestDto.getTaskId())) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }

        commentService.createComment(userPrincipal.getId(), requestDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление комментария")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "id") Integer id) {
        commentService.deleteComment(id);

        return ResponseEntity.ok("ok");
    }
}

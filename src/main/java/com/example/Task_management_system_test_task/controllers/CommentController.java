package com.example.Task_management_system_test_task.controllers;

import com.example.Task_management_system_test_task.dtos.CommentCreateRequestDto;
import com.example.Task_management_system_test_task.security.UserDetailsImpl;
import com.example.Task_management_system_test_task.services.AuthService;
import com.example.Task_management_system_test_task.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.ACCESS_DENIED;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<String> createComment(Authentication authentication,
                                                @RequestBody CommentCreateRequestDto requestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();
        if (!authService.hasAccessToCreateComment(userDetails, requestDto.getTaskId())) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }

        commentService.createComment(userDetails.getId(), requestDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "id") Integer id) {
        commentService.deleteComment(id);

        return ResponseEntity.ok("ok");
    }
}

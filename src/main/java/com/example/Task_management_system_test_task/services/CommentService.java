package com.example.Task_management_system_test_task.services;

import com.example.Task_management_system_test_task.dtos.CommentCreateRequestDto;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.repos.CommentRepository;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.tables.Comment;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.TASK_NOT_FOUND;
import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public void createComment(Integer currentUserId, CommentCreateRequestDto requestDto) {
        Task task = taskRepository.findById(requestDto.getTaskId()).orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        Comment comment = new Comment();

        comment.setUser(user);
        comment.setTask(task);
        comment.setValue(requestDto.getComment());

        commentRepository.save(comment);
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}

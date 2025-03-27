package com.example.Task_management_system_test_task.unit_tests.services;

import com.example.Task_management_system_test_task.dtos.CommentCreateRequestDto;
import com.example.Task_management_system_test_task.repos.CommentRepository;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.services.CommentService;
import com.example.Task_management_system_test_task.tables.Comment;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

public class CommentServiceUnitTests {
    private final CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final CommentService commentService = new CommentService(commentRepository, taskRepository, userRepository);

    @ParameterizedTest
    @MethodSource("provideData_for_test_createComment_validParams_success")
    public void test_createComment_validParams_success(Integer currentUserId, CommentCreateRequestDto requestDto, Task task, User user, Comment comment) {
        Mockito.when(taskRepository.findById(requestDto.getTaskId())).thenReturn(Optional.ofNullable(task));
        Mockito.when(userRepository.findById(currentUserId)).thenReturn(Optional.ofNullable(user));
        Mockito.when(commentRepository.save(Mockito.argThat(new CommentMatcher(comment)))).thenReturn(Mockito.any());

        commentService.createComment(currentUserId, requestDto);

        Mockito.verify(taskRepository, Mockito.times(1)).findById(requestDto.getTaskId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(currentUserId);
        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.argThat(new CommentMatcher(comment)));
    }

    @Test
    public void test_deleteComment_validParams_success() {
        Integer commentId = 1;

        commentService.deleteComment(commentId);

        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(commentId);
    }

    private static Stream<Arguments> provideData_for_test_createComment_validParams_success() {
        Integer currentUserId = 1;

        Integer taskId = 1;

        CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto();
        commentCreateRequestDto.setComment("aaa");
        commentCreateRequestDto.setTaskId(taskId);

        Task task = new Task();
        task.setId(taskId);

        User user = new User();
        user.setId(currentUserId);

        Comment comment = new Comment();

        comment.setValue(commentCreateRequestDto.getComment());
        comment.setUser(user);
        comment.setTask(task);

        return Stream.of(
                Arguments.of(currentUserId, commentCreateRequestDto, task, user, comment)
        );
    }

    private static class CommentMatcher implements ArgumentMatcher<Comment> {
        private final Comment left;

        public CommentMatcher(Comment left) {
            this.left = new Comment();

            this.left.setValue(left.getValue());
            this.left.setUser(left.getUser());
            this.left.setTask(left.getTask());
        }

        @Override
        public boolean matches(Comment right) {
            return left.getValue().equals(right.getValue())
                    && left.getUser().getId().equals(right.getUser().getId())
                    && left.getTask().getId().equals(right.getTask().getId());
        }
    }
}

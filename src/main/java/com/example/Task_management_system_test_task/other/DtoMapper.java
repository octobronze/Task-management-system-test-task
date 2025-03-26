package com.example.Task_management_system_test_task.other;

import com.example.Task_management_system_test_task.dtos.CommentGetResponseDto;
import com.example.Task_management_system_test_task.dtos.TaskGetResponseDto;
import com.example.Task_management_system_test_task.tables.Comment;
import com.example.Task_management_system_test_task.tables.Task;

public class DtoMapper {
    public static TaskGetResponseDto taskToTaskGetResponseDto(Task task) {
        TaskGetResponseDto responseDto = new TaskGetResponseDto();

        responseDto.setId(task.getId());
        responseDto.setTitle(task.getTitle());
        responseDto.setDescription(task.getDescription());
        responseDto.setStatusIndex(task.getStatus().ordinal());
        responseDto.setPriorityIndex(task.getPriority().ordinal());
        responseDto.setCreatorId(task.getCreator().getId());
        responseDto.setImplementerId(task.getImplementer().getId());
        responseDto.setComments(task.getComments().stream().map(DtoMapper::commentToCommentGetResponseDto).toList());

        return responseDto;
    }

    public static CommentGetResponseDto commentToCommentGetResponseDto(Comment comment) {
        CommentGetResponseDto responseDto = new CommentGetResponseDto();

        responseDto.setId(comment.getId());
        responseDto.setValue(comment.getValue());
        responseDto.setUserId(comment.getUser().getId());

        return responseDto;
    }
}

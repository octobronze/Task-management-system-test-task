package com.example.Task_management_system_test_task.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TaskGetResponseDto {
    private Integer id;
    private String title;
    private String description;
    private Integer statusIndex;
    private Integer priorityIndex;
    private Integer implementerId;
    private Integer creatorId;
    private List<CommentGetResponseDto> comments;
}

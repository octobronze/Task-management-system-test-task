package com.example.Task_management_system_test_task.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TaskCreateRequestDto {
    private String title;
    private String description;
    private Integer taskStatusIndex;
    private Integer priorityIndex;
    private String comments;
    private Integer implementerId;
}

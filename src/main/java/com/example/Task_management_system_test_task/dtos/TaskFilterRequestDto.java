package com.example.Task_management_system_test_task.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TaskFilterRequestDto extends PageDto {
    private Integer implementerId;
    private Integer creatorId;
}

package com.example.Task_management_system_test_task.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TaskUpdateByAdminRequestDto extends TaskUpdateRequestDto {
    private Integer priorityIndex;
    private Integer implementerId;
}

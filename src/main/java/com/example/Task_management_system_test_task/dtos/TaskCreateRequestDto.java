package com.example.Task_management_system_test_task.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TaskCreateRequestDto {
    @NotBlank(message = "title cannot be blank")
    private String title;
    @NotBlank(message = "description cannot be blank")
    private String description;
    @NotNull(message = "priorityIndex cannot be null")
    private Integer priorityIndex;
    @NotNull(message = "implementerId cannot be null")
    private Integer implementerId;
}

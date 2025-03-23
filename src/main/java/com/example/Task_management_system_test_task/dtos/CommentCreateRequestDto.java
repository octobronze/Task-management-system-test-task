package com.example.Task_management_system_test_task.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CommentCreateRequestDto {
    @NotNull(message = "taskId cannot be null")
    private Integer taskId;
    @NotBlank(message = "comment cannot be blank")
    private String comment;
}

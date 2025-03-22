package com.example.Task_management_system_test_task.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentGetResponseDto {
    private Integer id;
    private String value;
    private Integer userId;
}

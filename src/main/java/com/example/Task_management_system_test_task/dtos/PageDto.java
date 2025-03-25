package com.example.Task_management_system_test_task.dtos;

import jakarta.validation.constraints.Min;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class PageDto {
    @Min(value = 0, message = "pageIndex cannot be less than 0")
    private Integer pageIndex;
    @Min(value = 1, message = "pageSize cannot be less than 1")
    private Integer pageSize;
}

package com.example.Task_management_system_test_task.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}

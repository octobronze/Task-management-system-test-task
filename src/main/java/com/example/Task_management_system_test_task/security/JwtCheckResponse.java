package com.example.Task_management_system_test_task.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(force = true)
public class JwtCheckResponse {
    private final boolean isValid;
    private final String responseString;
}

package com.example.Task_management_system_test_task.services;

import com.example.Task_management_system_test_task.dtos.LoginRequestDto;
import com.example.Task_management_system_test_task.dtos.LoginResponseDto;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.security.JwtService;
import com.example.Task_management_system_test_task.security.UserDetailsImpl;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.TASK_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TaskRepository taskRepository;

    @Value("${jwt.duration}")
    private Long jwtDuration;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );

        String jwtToken = jwtService.generateTokenForUser((User) authentication.getDetails());

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(jwtToken);
        loginResponseDto.setLifeTimeSec(jwtDuration);

        return loginResponseDto;
    }

    public boolean hasAccessToCreateComment(UserDetailsImpl userDetails, Integer taskId) {
        if (userDetails.isAdmin()) return true;

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));

        return task.getImplementer().getId().equals(userDetails.getId());
    }

    public boolean hasAccessToUpdateTask(UserDetailsImpl userDetails, Integer taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));

        return task.getImplementer().getId().equals(userDetails.getId());
    }
}

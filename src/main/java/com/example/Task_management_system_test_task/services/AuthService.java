package com.example.Task_management_system_test_task.services;

import com.example.Task_management_system_test_task.dtos.LoginRequestDto;
import com.example.Task_management_system_test_task.dtos.LoginResponseDto;
import com.example.Task_management_system_test_task.enums.EntityFieldEnum;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.repos.TaskRepository;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.security.JwtService;
import com.example.Task_management_system_test_task.security.UserPrincipal;
import com.example.Task_management_system_test_task.specifications.FetchService;
import com.example.Task_management_system_test_task.specifications.TaskSpecification;
import com.example.Task_management_system_test_task.specifications.UserSpecification;
import com.example.Task_management_system_test_task.tables.Task;
import com.example.Task_management_system_test_task.tables.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.BAD_CREDENTIALS;
import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.duration}")
    private Long jwtDuration;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        UserPrincipal userPrincipal = authenticateUser(requestDto.getEmail(), requestDto.getPassword());

        String jwtToken = jwtService.generateTokenByUserPrincipal(userPrincipal);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(jwtToken);
        loginResponseDto.setLifeTimeMs(jwtDuration);

        return loginResponseDto;
    }

    public boolean hasAccessToCreateComment(UserPrincipal userPrincipal, Integer taskId) {
        if (userPrincipal.isAdmin()) return true;

        Task task = taskRepository.findOne(TaskSpecification.builder()
                .id(taskId)
                .fetchService(new FetchService<>(EntityFieldEnum.IMPLEMENTER)).build()
        ).orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));

        return task.getImplementer().getId().equals(userPrincipal.getId());
    }

    public boolean hasAccessToUpdateTask(UserPrincipal userPrincipal, Integer taskId) {
        Task task = taskRepository.findOne(TaskSpecification.builder()
                .id(taskId)
                .fetchService(new FetchService<>(EntityFieldEnum.IMPLEMENTER)).build()
        ).orElseThrow(() -> new BadRequestException(TASK_NOT_FOUND));

        return task.getImplementer().getId().equals(userPrincipal.getId());
    }

    private UserPrincipal authenticateUser(String email, String password) {
        User user = userRepository.findOne(UserSpecification.builder()
                .email(email)
                .fetchService(new FetchService<>(EntityFieldEnum.ROLE)).build()
        ).orElseThrow(() -> new BadRequestException(BAD_CREDENTIALS));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(BAD_CREDENTIALS);
        }

        return new UserPrincipal(user);
    }
}

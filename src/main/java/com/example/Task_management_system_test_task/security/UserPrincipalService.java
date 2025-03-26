package com.example.Task_management_system_test_task.security;

import com.example.Task_management_system_test_task.enums.entity_fetch_fields.UserFetchFields;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.specifications.UserSpecification;
import com.example.Task_management_system_test_task.tables.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.List;

import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.USER_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class UserPrincipalService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserPrincipal createUserPrincipalWithJwt(String jwt) {
        Integer userId = jwtService.getUserId(jwt);
        User user = userRepository.findOne(UserSpecification.builder()
                .id(userId)
                .fetchFields(List.of(UserFetchFields.ROLE)).build()
        ).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        UserPrincipal userPrincipal = new UserPrincipal();

        userPrincipal.setId(userId);
        userPrincipal.setUsername(user.getEmail());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setAuthorities(user.getRole().getName());

        return userPrincipal;
    }
}

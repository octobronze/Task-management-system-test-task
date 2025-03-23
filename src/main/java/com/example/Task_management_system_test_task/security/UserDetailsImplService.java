package com.example.Task_management_system_test_task.security;

import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import com.example.Task_management_system_test_task.repos.UserRepository;
import com.example.Task_management_system_test_task.tables.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import static com.example.Task_management_system_test_task.consts.ExceptionMessagesConsts.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserDetailsImplService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserDetails createUserDetailsImplWithJwt(String jwt) {
        Integer userId = jwtService.getUserId(jwt);
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        UserDetailsImpl userDetails = new UserDetailsImpl();

        userDetails.setId(userId);
        userDetails.setUsername(user.getEmail());
        userDetails.setPassword(user.getPassword());
        userDetails.setAuthorities(user.getRole().getName());

        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
    }
}

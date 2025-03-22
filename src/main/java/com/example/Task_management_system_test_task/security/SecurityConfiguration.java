package com.example.Task_management_system_test_task.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import static com.example.Task_management_system_test_task.consts.RequestMatchers.ADMIN_ENDPOINTS;
import static com.example.Task_management_system_test_task.consts.RequestMatchers.USER_ENDPOINTS;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .anyRequest().permitAll()
                                .requestMatchers(ADMIN_ENDPOINTS).hasRole("Admin")
                                .requestMatchers(USER_ENDPOINTS).hasAnyRole("User", "Admin")
                )
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.example.Task_management_system_test_task.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import static com.example.Task_management_system_test_task.consts.SecurityEndpoints.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final SecurityExceptionHandler securityExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(securityExceptionHandler)
                )
                .authorizeHttpRequests(
                        authorizeHttpRequests ->
                            authorizeHttpRequests
                                    .requestMatchers(HttpMethod.POST, ADMIN_ENDPOINTS_POST).hasRole("Admin")
                                    .requestMatchers(HttpMethod.PUT, ADMIN_ENDPOINTS_PUT).hasRole("Admin")
                                    .requestMatchers(HttpMethod.DELETE, ADMIN_ENDPOINTS_DELETE).hasRole("Admin")
                                    .requestMatchers(HttpMethod.PUT, USER_ENDPOINTS_PUT).hasAnyRole("User", "Admin")
                                    .requestMatchers(HttpMethod.GET, USER_ENDPOINTS_GET).hasAnyRole("User", "Admin")
                                    .requestMatchers(HttpMethod.POST, USER_ENDPOINTS_POST).hasAnyRole( "User", "Admin")
                                    .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)
                .sessionManagement(management -> management.sessionCreationPolicy(STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}

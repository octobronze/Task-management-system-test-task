package com.example.Task_management_system_test_task.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import static com.example.Task_management_system_test_task.consts.SecurityEndpoints.*;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST, ADMIN_ENDPOINTS_POST).hasRole("Admin")
                                .requestMatchers(HttpMethod.PUT, ADMIN_ENDPOINTS_PUT).hasRole("Admin")
                                .requestMatchers(HttpMethod.DELETE, ADMIN_ENDPOINTS_DELETE).hasRole("Admin")
                                .requestMatchers(HttpMethod.PUT, USER_ENDPOINTS_PUT).hasAnyRole("User", "Admin")
                                .requestMatchers(HttpMethod.GET, USER_ENDPOINTS_GET).hasAnyRole("User", "Admin")
                                .anyRequest().permitAll()

//                                .requestMatchers(ADMIN_ENDPOINTS).hasRole("Admin")
//                                .requestMatchers(USER_ENDPOINTS).hasAnyRole("User", "Admin")
                )
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /*@Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return
    }*/
}

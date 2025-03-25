package com.example.Task_management_system_test_task.swagger;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.Task_management_system_test_task.consts.SecurityEndpoints.SWAGGER_ENDPOINTS;

@Configuration
@RequiredArgsConstructor
public class SwaggerSecurityConfiguration {
    @Value("${swagger.username}")
    private String swaggerUsername;
    @Value("${swagger.password}")
    private String swaggerPassword;

    @Bean
    @Order(1)
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .securityMatcher(SWAGGER_ENDPOINTS)
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .anyRequest().authenticated()
                )
                .authenticationProvider(swaggerAuthenticationProvider())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    public AuthenticationProvider swaggerAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        daoAuthenticationProvider.setUserDetailsService(swaggerUserDetailsService());
        return daoAuthenticationProvider;
    }

    public UserDetailsService swaggerUserDetailsService() {
        UserDetails user = User.withUsername(swaggerUsername)
                .password(swaggerPassword)
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

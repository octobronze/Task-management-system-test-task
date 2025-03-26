package com.example.Task_management_system_test_task.security;

import com.example.Task_management_system_test_task.exceptions.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception instanceof JwtException) {
            response.getWriter().write("Invalid JWT");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (exception instanceof InsufficientAuthenticationException){
            response.getWriter().write("Unauthorized. Is token provided?");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        if (exception instanceof AuthorizationDeniedException) {
           response.getWriter().write("Access denied. Does user have permission to access resource?");
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

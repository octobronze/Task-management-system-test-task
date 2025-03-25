package com.example.Task_management_system_test_task.security;

import com.example.Task_management_system_test_task.exceptions.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER_NAME = "authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserPrincipalService userPrincipalService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeaderValue = request.getHeader(AUTHORIZATION_HEADER_NAME);

        if (authHeaderValue == null
                || !authHeaderValue.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeaderValue.substring(TOKEN_PREFIX.length());
        request.getHeader(AUTHORIZATION_HEADER_NAME);

        JwtCheckResponse jwtCheckResponse = jwtService.checkUserToken(token);

        if (!jwtCheckResponse.isValid()) {
            throw new JwtException(jwtCheckResponse.responseString());
        }

        UserPrincipal userPrincipal = userPrincipalService.createUserPrincipalWithJwt(token);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}

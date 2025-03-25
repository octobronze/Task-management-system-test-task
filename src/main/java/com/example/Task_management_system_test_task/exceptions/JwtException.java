package com.example.Task_management_system_test_task.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtException extends AuthenticationException {
  public JwtException(String message) {
    super(message);
  }
}

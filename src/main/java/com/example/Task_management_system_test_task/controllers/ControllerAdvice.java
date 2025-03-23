package com.example.Task_management_system_test_task.controllers;

import com.example.Task_management_system_test_task.dtos.ExceptionResponseDto;
import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ExceptionResponseDto> handleBadRequestException(BadRequestException exception) {
        ExceptionResponseDto response = new ExceptionResponseDto();

        response.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleException(Exception exception) {
        ExceptionResponseDto response = new ExceptionResponseDto();

        response.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ExceptionResponseDto response = new ExceptionResponseDto();

        response.setMessage(extractDefaultMessageFromValidationException(exception));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private String extractDefaultMessageFromValidationException(MethodArgumentNotValidException exception) {
        return Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
    }
}
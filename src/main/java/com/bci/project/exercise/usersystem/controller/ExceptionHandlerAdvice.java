package com.bci.project.exercise.usersystem.controller;

import com.bci.project.exercise.usersystem.dto.error.ResponseDTO;
import com.bci.project.exercise.usersystem.dto.error.ResponseDetailDTO;
import com.bci.project.exercise.usersystem.enums.CodeType;
import com.bci.project.exercise.usersystem.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.info("Validation error.");

        final LocalDateTime now = LocalDateTime.now();
        final List<ResponseDetailDTO> details = new ArrayList<>();
        final List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        errors.forEach(error -> {
            ResponseDetailDTO detail = new ResponseDetailDTO();
            detail.setTimestamp(now);
            detail.setCode(CodeType.VALIDATION.getCode());
            detail.setDetail(error);
            details.add(detail);
        });

        return new ResponseEntity<>(new ResponseDTO(details), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResponseDTO> handlerApiException(final ApiException exception) {
        log.info("API error. Message: {}", exception.getMessage());

        return new ResponseEntity<>(exception.getResponse(), exception.getHttpStatus());
    }

}

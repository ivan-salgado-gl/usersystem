package com.bci.project.exercise.usersystem.exception;

import com.bci.project.exercise.usersystem.dto.error.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends Exception {

    private final ResponseDTO response;
    private final HttpStatus httpStatus;

    public ApiException(final String message, final ResponseDTO response, final HttpStatus httpStatus) {
        super(message);
        this.response = response;
        this.httpStatus = httpStatus;
    }

}

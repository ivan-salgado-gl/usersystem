package com.bci.project.exercise.usersystem.enums;

import org.springframework.http.HttpStatus;

public enum CodeType {

    VALIDATION(1000, "ERROR IN THE VALIDATIONS", HttpStatus.BAD_REQUEST),

    EXISTING_RECORD(2000, "EXISTING RECORD", HttpStatus.CONFLICT),
    RECORD_NOT_FOUND(2001, "RECORD NOT FOUND", HttpStatus.NOT_FOUND),
    GENERIC_ERROR(2002, "GENERIC ERROR", HttpStatus.INTERNAL_SERVER_ERROR),

    ENCRYPTION_ERROR(3000, "ENCRYPTION ERROR", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_JWT_SIGNATURE(4000, "Invalid JWT signature", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_TOKEN(4001, "Invalid JWT token", HttpStatus.UNAUTHORIZED),
    EXPIRED_JWT_TOKEN(4002, "Expired JWT token", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_JWT_TOKEN(4003, "Unsupported JWT token", HttpStatus.UNAUTHORIZED),
    ILEGAL_JWT_CLAIMS(4004, "JWT claims string is empty", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatus http;

    CodeType(final int code, final String message, final HttpStatus http) {
        this.code = code;
        this.message = message;
        this.http = http;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttp() {
        return this.http;
    }

}

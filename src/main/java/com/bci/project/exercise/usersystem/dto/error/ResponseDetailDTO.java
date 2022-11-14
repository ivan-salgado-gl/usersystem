package com.bci.project.exercise.usersystem.dto.error;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDetailDTO {

    private LocalDateTime timestamp;
    private int code;
    private String detail;

}

package com.bci.project.exercise.usersystem.util;

import com.bci.project.exercise.usersystem.dto.error.ResponseDTO;
import com.bci.project.exercise.usersystem.dto.error.ResponseDetailDTO;
import com.bci.project.exercise.usersystem.enums.CodeType;
import com.bci.project.exercise.usersystem.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
public class ErrorManager {

    private ErrorManager() { }

    public static ApiException createError(final CodeType codeType) {
        final LocalDateTime now = LocalDateTime.now();
        final ResponseDetailDTO detail = new ResponseDetailDTO();

        detail.setCode(codeType.getCode());
        detail.setTimestamp(now);
        detail.setDetail(codeType.getMessage());


        final ResponseDTO response = new ResponseDTO(Collections.singletonList(detail));

        return new ApiException(codeType.getMessage(), response, codeType.getHttp());
    }

}

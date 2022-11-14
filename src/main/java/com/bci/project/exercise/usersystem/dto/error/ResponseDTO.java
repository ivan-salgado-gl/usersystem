package com.bci.project.exercise.usersystem.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private List<ResponseDetailDTO> errors;

}

package com.bci.project.exercise.usersystem.dto.response;

import lombok.Data;

@Data
public class PhoneDTO {

    private Long number;
    private Integer cityCode;
    private String countryCode;

}
